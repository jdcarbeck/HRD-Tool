import React, { Component } from 'react'
import styled from 'styled-components'

import Logo from './Logo'
import Links from './Links'


const Nav = styled.nav.attrs({
    className: 'navbar navbar-expand-sm navbar-dark',
})`
    margin-bottom: 20px;
    background-color: #131515;
    box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
`

class NavBar extends Component {
    render() {
        return (
                <Nav>
                    <Logo/>
                    <Links/>
                </Nav>
        )
    }
}

export default NavBar
