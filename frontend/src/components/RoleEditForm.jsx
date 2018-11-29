import React, { Component } from 'react';
import { Modal, FormControl, Button, FormGroup, ControlLabel, HelpBlock } from 'react-bootstrap'
import MessageBox from './MessageBox'

class RoleEditForm extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isSaving: false,
      isDeleting: false,
      error: null,
      role: {
        id: null,
        title: "",
        description: ""
      }
    };
    if (props.role) {
      this.state.role = props.role;
    }

    this.handleSaveButtonClicked = this.handleSaveButtonClicked.bind(this);
    this.handleDeleteButtonClicked = this.handleDeleteButtonClicked.bind(this);
    this.handleDescriptionChange = this.handleDescriptionChange.bind(this);
    this.handleTitleChange = this.handleTitleChange.bind(this);
  }

  componentWillReceiveProps(nextProps) {
    if (this.state.role !== nextProps.role) {
      this.setState({
        role: nextProps.role
      });
    }
  }

  handleSaveButtonClicked(e) {
    const { role } = this.state;
    const isNewRole = role.id == null;
    const params = {
      method: isNewRole ? "POST" : "PUT",
      body: JSON.stringify({
        title: role.title,
        description: role.description
      }),
      headers: new Headers({
        "Content-Type": "application/json;charset=utf-8"
      })
    };
    const url = this.props.host + "/roles/" + (isNewRole ? "" : role.id);
    this.setState({ isSaving: true });

    fetch(url, params)
      .then(response => response.json())
      .then(data => {
        let { role } = this.state;
        if (isNewRole) {
          role = Object.assign(role, { id: data.id });
          if (this.props.onCreate) {
            this.props.onCreate(role);
          }
          this.setState({ role, isSaving: false });
        } else {
          if (this.props.onChange) {
            this.props.onChange(role);
          }
          this.setState({ isSaving: false });
        }
      })
      .catch(console.log);
    e.preventDefault();
  }

  handleDeleteButtonClicked(e) {
    const { role } = this.state;
    const params = { method: "DELETE" };
    const url = this.props.host + `/roles/${role.id}`;
    this.setState({ isDeleting: true });
    fetch(url, params)
      .then(response => response.json())
      .then(data => {
        if (!data.success) {
          console.log(data.error);
          this.setState({ 
            isDeleting: false,
            error: "Невозможно удалить элемент. Возможно от него зависят другие данные?"
          });
          return;
        }
        if (this.props.onDelete) {
          this.props.onDelete(role);
        }
        this.setState({ isDeleting: false });
      })
      .catch(console.log);
    e.preventDefault();
  }

  getTitleValidationState() {
    const length = this.state.role.title.length;
    if (length > 10 || length === 0) {
      return "error";
    }
    return "success";
  }

  getDescriptionValidationState() {
    const length = this.state.role.description.length;
    if (length > 30 || length === 0) {
      return "error";
    }
    return "success";
  }

  handleTitleChange(e) {
    const title = e.target.value;
    let { role } = this.state;
    role = Object.assign(role, { title })
    this.setState({ role });
    e.preventDefault();
  }

  handleDescriptionChange(e) {
    const description = e.target.value;
    let { role } = this.state;
    role = Object.assign(role, { description })
    this.setState({ role });
    e.preventDefault();
  }

  render() {
    const titleValidationState = this.getTitleValidationState();
    const descriptionValidationState = this.getDescriptionValidationState();
    const isInputValid = titleValidationState === "success" && descriptionValidationState === "success";
    const { isSaving, isDeleting, error, role } = this.state;
    const isNewRole = this.props.role.id === null;

    return (
      <>
        <Modal show={this.props.show}>
          <Modal.Header>
            <Modal.Title>Изменить роль</Modal.Title>
          </Modal.Header>
          <form>
            <Modal.Body>
              <FormGroup
                validationState={titleValidationState}
              >
                <ControlLabel>Название</ControlLabel>
                <FormControl
                  type="text"
                  value={role.title}
                  placeholder="Введите название"
                  onChange={this.handleTitleChange}
                />
                <FormControl.Feedback />
                <HelpBlock>Название должно быть не пустым и не длиннее 10 символов</HelpBlock>
              </FormGroup>
              <FormGroup
                validationState={descriptionValidationState}
              >
                <ControlLabel>Описание</ControlLabel>
                <FormControl
                  type="text"
                  value={role.description}
                  placeholder="Введите описание"
                  onChange={this.handleDescriptionChange}
                />
                <FormControl.Feedback />
                <HelpBlock>Описание должно быть не пустым и не длиннее 30 символов</HelpBlock>
              </FormGroup>
            </Modal.Body>
            <Modal.Footer>
              <Button
                onClick={this.handleDeleteButtonClicked}
                bsStyle="danger"
                disabled={isNewRole || isDeleting || isSaving}
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

export default RoleEditForm;