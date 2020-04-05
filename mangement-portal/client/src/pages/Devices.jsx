import React, { Component } from 'react'
import { FormList } from '../components'
import styled from 'styled-components'
import Popup from "reactjs-popup";

const Modal = styled.div`
    margin-bottom: 20px;
    background-color: #FFFFFF;
`

// const Close = styled.button.attrs({
//     className: 'btn btn-danger'
// })`
//     cursor: pointer;
//     position: absolute;
//     display: block;
//     padding: 2px 10px;
//     line-height: 30px;
//     right: -10px;
//     top: -10px;
//     font-size: 24px;
// `

const Header = styled.div`
    width: 100%;
    border-bottom: 1px solid gray;
    font-size: 18px;
    text-align: center;
    padding: 5px;
`

const Content = styled.div`
    width: 100%;
    padding: 10px 5px;
`

const Actions = styled.div`
    width: 100%;
    padding: 10px 5px;
    margin: auto;
    text-align: center;
` 



class Devices extends Component {
    render() {
        return (
            <span>This is the device mangement screen</span>
        )
    }
}

export default Devices