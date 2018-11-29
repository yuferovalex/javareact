import ReactTable from 'react-table'
import React, { Component } from 'react';
import "react-table/react-table.css";
import { Button, ButtonToolbar } from 'react-bootstrap'
import UserEditForm from './UserEditForm';
import MessageBox from './MessageBox';

export default class UserTable extends Component {
  constructor(props) {
    super(props);

    this.state = {
      data: [],
      error: null,
      loading: true,
      edit: false,
      roles: [],
      currentUser: {
        id: null,
        name: "",
        role: {
          id: null,
          title: "",
          description: ""
        }
      }
    };

    this.handleAddButtonClicked = this.handleAddButtonClicked.bind(this);
    this.handleHideEditDialog = this.handleHideEditDialog.bind(this);
    this.handleCreateRole = this.handleCreateRole.bind(this);
    this.handleEditRole = this.handleEditRole.bind(this);
    this.handleDeleteRole = this.handleDeleteRole.bind(this);
    this.handleTableRowSelect = this.handleTableRowSelect.bind(this);
  }

  fetchData() {
    fetch(this.props.host + "/users", { method: "GET" })
      .then(response => response.json())
      .then(data => this.setState({ data: data.data, loading: false }))
      .catch(reason => {
        alert("Ошибка при загрузке данных");
        console.log(reason);
      });

    fetch(this.props.host + "/roles", { method: "GET" })
      .then(response => response.json())
      .then(data => this.setState({ roles: data.data }))
      .catch(console.log);
  }

  handleAddButtonClicked(e) {
    if (this.state.roles.length === 0) {
      this.setState({ error: "Для создания пользователя необходимо создать хотя бы одну роль!" });
      return ;
    }
    this.setState({
      currentUser: {
        id: null,
        name: "",
        role: this.state.roles[0]
      },
      edit: true
    });
    e.preventDefault();
  }

  handleHideEditDialog() {
    this.setState({ edit: false });
  }

  handleCreateRole(user) {
    let { data } = this.state;
    data.push(user);
    this.setState({ data, edit: false });
  }

  handleEditRole(user) {
    let { data } = this.state;
    const index = data.findIndex(u => u.id === user.id);
    data[index] = user;
    this.setState({ data, edit: false });
  }

  handleDeleteRole(user) {
    let { data } = this.state;
    data.splice(data.indexOf(user), 1);
    this.setState({ data, edit: false });
  }

  handleTableRowSelect(rowInfo) {
    if (typeof rowInfo === 'undefined') {
      return;
    }
    const { data } = this.state;
    this.setState({
      currentUser: data[rowInfo.index],
      edit: true
    });
  }

  render() {
    const columns = [
      {
        Header: "Имя",
        accessor: "name"
      },
      {
        Header: "Роль",
        accessor: "role.title"
      },
      {
        Header: "Описание",
        accessor: "role.description"
      }
    ];

    const { data, loading, edit, currentUser, roles, error } = this.state;
    return (
      <>
        <ButtonToolbar>
          <Button className="pull-right" onClick={this.handleAddButtonClicked}>Добавить</Button>
        </ButtonToolbar>
        <br />
        <ReactTable
          columns={columns}
          data={data}
          loading={loading}
          onFetchData={this.fetchData.bind(this)}
          getTdProps={(state, rowInfo, column, instance) => {
            return {
              onClick: (e) => {
                this.handleTableRowSelect(rowInfo);
              }
            }
          }}
        />
        <UserEditForm 
          show={edit} 
          host={this.props.host} 
          user={currentUser}
          roles={roles}
          onHide={this.handleHideEditDialog}
          onCreate={this.handleCreateRole} 
          onDelete={this.handleDeleteRole}
          onChange={this.handleEditRole}
        />
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