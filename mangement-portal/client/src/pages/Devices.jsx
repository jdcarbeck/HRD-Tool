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
            <Popup trigger={<button className="btn btn-outline-success"> Open Modal </button>} modal>
                {close => (
                <Modal>
                    <Header> Modal Title </Header>
                    <Content>
                    {" "}
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Atque, a nostrum.
                    Dolorem, repellat quidem ut, minima sint vel eveniet quibusdam voluptates
                    delectus doloremque, explicabo tempore dicta adipisci fugit amet dignissimos?
                    <br />
                    Lorem ipsum dolor sit amet, consectetur adipisicing elit. Consequatur sit
                    commodi beatae optio voluptatum sed eius cumque, delectus saepe repudiandae
                    explicabo nemo nam libero ad, doloribus, voluptas rem alias. Vitae?
                    </Content>
                    <Actions>
                    <Popup
                        trigger={<button className="btn btn-outline-primary"> Trigger </button>}
                        position="top center"
                        closeOnDocumentClick
                    >
                        <span>
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Beatae
                        magni omnis delectus nemo, maxime molestiae dolorem numquam
                        mollitia, voluptate ea, accusamus excepturi deleniti ratione
                        sapiente! Laudantium, aperiam doloribus. Odit, aut.
                        </span>
                    </Popup>
                    <button
                        className="btn btn-outline-secondary"
                        onClick={() => {
                        console.log("modal closed ");
                        close();
                        }}
                    >
                        close
                    </button>
                    </Actions>
                </Modal>
                )}
            </Popup>
        )
    }
}

export default Devices