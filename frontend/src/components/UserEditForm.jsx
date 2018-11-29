import React, { Component } from 'react';
import { Modal, FormControl, Button, FormGroup, ControlLabel, HelpBlock } from 'react-bootstrap'
import MessageBox from './MessageBox'

class UserEditForm extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isSaving: false,
      isDeleting: false,
      error: null,
      user: {
        id: null,
        name: "",
        role: {
          id: null,
          title: "",
          description: ""
        }
      }
    };
    if (props.role) {
      this.state.user = props.user;
    }

    this.handleSaveButtonClicked = this.handleSaveButtonClicked.bind(this);
    this.handleDeleteButtonClicked = this.handleDeleteButtonClicked.bind(this);
    this.handleNameChange = this.handleNameChange.bind(this);
    this.handleRoleChange = this.handleRoleChange.bind(this);
  }

  componentWillReceiveProps(nextProps) {
    if (this.state.user !== nextProps.user) {
      this.setState({
        user: nextProps.user
      });
    }
  }

  handleSaveButtonClicked(e) {
    const { user } = this.state;
    const isNewUser = user.id == null;
    const params = {
      method: isNewUser ? "POST" : "PUT",
      body: JSON.stringify({
        name: user.name.trim(),
        roleId: user.role.id
      }),
      headers: new Headers({
        "Content-Type": "application/json;charset=utf-8"
      })
    };
    const url = this.props.host + "/users/" + (isNewUser ? "" : user.id);
    this.setState({ isSaving: true });
    fetch(url, params)
      .then(response => response.json())
      .then(data => {
        let { user } = this.state;
        if (!data.success) {
          console.log(data.error);
          this.setState({
            error: "Не удалось сохранить данные. Ответ сервера: " + data.error,
            isDeleting: false
          });
          return;
        }
        if (isNewUser) {
          user = Object.assign(user, { id: data.id });
          if (this.props.onCreate) {
            this.props.onCreate(user);
          }
          this.setState({ user, isSaving: false });
        } else {
          if (this.props.onChange) {
            this.props.onChange(user);
            this.setState({ isSaving: false });
          }
        }
      })
      .catch(console.log);
    e.preventDefault();
  }

  handleDeleteButtonClicked(e) {
    const { user } = this.state;
    const params = { method: "DELETE" };
    const url = this.props.host + `/users/${user.id}`;
    this.setState({ isDeleting: true });
    fetch(url, params)
      .then(response => response.json())
      .then(data => {
        if (!data.success) {
          console.log(data.error);
          this.setState({
            error: "Невозможно удалить элемент. Возможно от него зависят другие данные?",
            isDeleting: false
          });
          return;
        }
        if (this.props.onDelete) {
          this.props.onDelete(user);
        }
        this.setState({ isDeleting: false });
      })
      .catch(console.log);
    e.preventDefault();
  }

  getNameValidationState() {
    const name = this.state.user.name.trim();
    const length = name.length;
    const regex = /^([a-zа-я]+(-[a-zа-я]+)?)(\s{1}([a-zа-я]+(-[a-zа-я]+)?))?$/i.test(name);
    if (length > 30 || length === 0 || !regex) {
      return "error";
    }
    return "success";
  }

  handleNameChange(e) {
    let { user } = this.state;
    user.name = e.target.value;
    this.setState({ user });
    e.preventDefault();
  }

  handleRoleChange(e) {
    let { user } = this.state;
    const id = parseInt(e.target.value);
    user.role = this.props.roles.find(r => r.id == id);
    this.setState({ user });
    e.preventDefault();
  }

  render() {
    const nameValidationState = this.getNameValidationState();
    const isInputValid = nameValidationState === "success";
    const { isSaving, isDeleting, error, user } = this.state;
    const isNewUser = this.state.user.id === null;

    const roles = this.props.roles.map(role => 
        <option key={role.id} value={role.id}>{role.title}</option>
    );

    return (
      <>
        <Modal show={this.props.show}>
          <Modal.Header>
            <Modal.Title>Изменить пользователя</Modal.Title>
          </Modal.Header>
          <form>
            <Modal.Body>
              <FormGroup validationState={nameValidationState}>
                <ControlLabel>Полное имя</ControlLabel>
                <FormControl
                  type="text"
                  value={user.name}
                  placeholder="Введите имя"
                  onChange={this.handleNameChange}
                />
                <FormControl.Feedback />
                <HelpBlock>
                  Полное имя должно состоять из букв и одного пробела,
                  разделяющего имя и фамилию, и быть не длиннее 30 символов
                </HelpBlock>
              </FormGroup>
              <FormGroup>
                <ControlLabel>Роль пользователя</ControlLabel>
                <FormControl 
                  componentClass="select" 
                  onChange={this.handleRoleChange} 
                  value={user.role.id}
                >
                  {roles}
                </FormControl>
              </FormGroup>
            </Modal.Body>
            <Modal.Footer>
              <Button
                onClick={this.handleDeleteButtonClicked}
                bsStyle="danger"
                disabled={isNewUser || isDeleting || isSaving}
                className="pull-left"
              >
                Удалить
            </Button>
              <Button
                onClick={this.handleSaveButtonClicked}
                disabled={isDeleting || isSaving || !isInputValid}
                bsStyle="primary"
              >
                Сохранить
            </Button>
              <Button onClick={this.props.onHide}>Отмена</Button>
            </Modal.Footer>
          </form>
        </Modal>
        <MessageBox
          title="Произошла ошибка"
          show={error !== null}
          message={error}
          onHide={() => this.setState({ error: null })}
        />
      </>
    );
  }
}

export default UserEditForm;