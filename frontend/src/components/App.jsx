import React, { Component } from 'react';
import './CommonTable'
import './RoleTable'
import CommonTable from './CommonTable';
import { Navbar, Nav, NavItem } from 'react-bootstrap'
import RoleTable from './RoleTable';
import UserTable from './UserTable';
import { Switch, Route } from 'react-router'
import { LinkContainer } from 'react-router-bootstrap';

const HOST = "http://localhost:8080"

const _CommonTable = () => (
  <CommonTable host={HOST} />
)

const _UserTable = () => (
  <UserTable host={HOST} />
)

const _RoleTable = () => (
  <RoleTable host={HOST} />
)

const Main = () => (
  <main>
    <Switch>
      <Route exact path='/' component={_CommonTable}/>
      <Route path='/users' component={_UserTable}/>
      <Route path='/roles' component={_RoleTable}/>
    </Switch>
  </main>
)

class App extends Component {
  render() {
    return (
      <React.Fragment>
        <Navbar>
          <Nav>
            <LinkContainer exact to="/">
              <NavItem>Общая таблица</NavItem>
            </LinkContainer>
            <LinkContainer to="/users">
              <NavItem>Пользователи</NavItem>
            </LinkContainer>
            <LinkContainer to="/roles">
              <NavItem>Роли</NavItem>
            </LinkContainer>
          </Nav>
        </Navbar>
        <div className="container">
          <Main />
        </div>
      </React.Fragment>
    );
  }
}

export default App;
