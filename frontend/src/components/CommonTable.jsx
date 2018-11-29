import ReactTable from 'react-table'
import React, { Component } from 'react';
import "react-table/react-table.css";

class CommonTable extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: [],
      loading: true
    };
  }

  fetchData() {
    fetch(this.props.host + "/users")
      .then(response => response.json())
      .then(body => {
        this.setState({
          data: body.data,
          loading: false
        });
      }).catch(reason => {
        alert("Не удалось загрузить данные: " + reason);
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

    const { loading, data } = this.state;

    return (
      <>
        <ReactTable
          loading = { loading }
          data = { data } 
          columns = { columns }
          onFetchData = { this.fetchData.bind(this) } />
      </>
    );
  }
}

export default CommonTable;