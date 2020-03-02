import React from 'react'
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'

import { NavBar } from '../components'
import { Dashboard } from '../pages'
import styled from 'styled-components'

import 'bootstrap/dist/css/bootstrap.min.css'

const Container = styled.div.attrs({
  className: 'container',
})``

function App() {
    return (
        <Router>
            <NavBar />
            <Container>
              <Switch>
                <Route path="/" exact component={ Dashboard } />
              </Switch>
            </Container>
        </Router>
    )
}

export default App
