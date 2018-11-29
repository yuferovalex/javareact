import ReactTable from 'react-table'
import React, { Component } from 'react';
import "react-table/react-table.css";
import { Button, ButtonToolbar } from 'react-bootstrap'
import RoleEditForm from './RoleEditForm';

class RoleTable extends Component {
  constructor(props) {
    super(props);

    this.state = {
      data: [],
      loading: true,
      edit: false,
      currentRole: {
        id: null,
        title: "",
        description: ""
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
    fetch(this.props.host + "/roles", { method: "GET" })
      .then(response => response.json())
      .then(data => this.setState({ data: data.data, loading: false }))
      .catch(reason => {
        alert("Ошибка при загрузке данных");
        console.log(reason);
      });
  }

  handleAddButtonClicked(e) {
    this.setState({
      currentRole: {
        id: null,
        title: "",
        description: ""
      },
      edit: true
    });
    e.preventDefault();
  }

  handleHideEditDialog() {
    this.setState({ edit: false });
  }

  handleCreateRole(role) {
    let { data } = this.state;
    data.push(role);
    this.setState({ data, edit: false });
  }

  handleEditRole(role) {
    let { data } = this.state;
    const index = data.findIndex(r => r.id === role.id);
    data[index] = role;
    this.setState({ data, edit: false });
  }

  handleDeleteRole(role) {
    let { data } = this.state;
    data.splice(data.indexOf(role), 1);
    this.setState({ data, edit: false });
  }

  handleTableRowSelect(rowInfo) {
    if (typeof rowInfo === 'undefined') {
      return;
    }
    const { data } = this.state;
    this.setState({
      currentRole: data[rowInfo.index],
      edit: true
    });
  }

  render() {
    const columns = [
      {
        Header: "Роль",
        accessor: "title"
      },
      {
        Header: "Описание",
        accessor: "description"
      }
    ];

    const { data, loading, edit, currentRole } = this.state;
    return (
      <React.Fragment>
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
        <RoleEditForm 
          show={edit} 
          host={this.props.host} 
          role={currentRole} 
          onHide={this.handleHideEditDialog}
          onCreate={this.handleCreateRole} 
          onDelete={this.handleDeleteRole}
          onChange={this.handleEditRole}
        />
      </React.Fragment>
    );
  }
}

export default RoleTable;