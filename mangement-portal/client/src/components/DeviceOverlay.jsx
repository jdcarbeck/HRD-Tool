import React, { Component } from 'react'
import styled from 'styled-components'
import Popup from 'reactjs-popup'
import api from '../api'

const Modal = styled.div`
margin-bottom: 20px;
background-color: #FFFFFF;
border-radius: 5px;
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

const Close = styled.a`
    cursor: pointer;
    position: absolute;
    display: block;
    padding: 2px 5px;
    line-height: 20px;
    right: -10px;
    top: -10px;
    font-size: 24px;
    background: #ffffff;
    border-radius: 18px;
    border: 1px solid #cfcece;
`


class Form extends Component {

    constructor(props) {
        super(props);
        this.state = { 
            _id: this.props.data._id,
            name: this.props.data.name,
            device_id: this.props.data.device_id,
            location: this.props.data.location, 
        };
    }
    
    changeName = (event) => {
        this.setState({
            name: event.target.value});
    }

    changeDevice = (event) => {
        console.log(event.target.value)
        this.setState({
            device_id: event.target.value});
    }

    changeLocation = (event) => {
        this.setState({
            location: event.target.value});
    }


    updateDevice = async () => {
        console.log(this.state._id)
        let obj = {
            name: this.state.name,
            device_id: this.state.device_id,
            location: this.state.location
        }
        await api.updateDevice(this.state._id, obj).then( () =>{
            this.props.callback()
            window.location.reload(false);
        })
    }

    newDevice = async () => {
        let obj = {
            name: this.state.name,
            device_id: this.state.device_id,
            location: this.state.location
        }
        await api.newDevice(obj).then( () =>{
            this.props.callback()
            window.location.reload(false);
        })
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-6">
                        <h4>{this.props.label}</h4>
                    </div>
                </div>
                <div className="row">
                    <div className="col-6">
                       <form>
                       <div className="form-group">
                            <label htmlFor="name">Name</label>
                            <input type="text" onChange={this.changeName} className="form-control" id="name" aria-describedby="nameHelp" value={this.state.name}/>
                            <small id="nameHelp" className="form-text text-muted">Name of human right defender using the device.</small>
                        </div>
                        <div className="form-group">
                            <label htmlFor="deviceID">Device ID</label>
                            <input type="text" onChange={this.changeDevice} className="form-control" id="deviceID" aria-describedby="deviceHelp" value={this.state.device_id}/>
                            <small id="deviceHelp" className="form-text text-muted">The device identifier of device being used by human rights defender</small>
                        </div>
                        <div className="form-group">
                            <label htmlFor="location">Location</label>
                            <input type="text" onChange={this.changeLocation} className="form-control" id="location" value={this.state.location}/>
                        </div>
                       </form>
                    </div>
                </div>   
                <div className="row">
                    <div className="col-6">
                        <button className="btn btn-outline-success mr-2" onClick={() => {
                            if(this.props.update){
                                this.updateDevice()
                            } 
                            else {
                                this.newDevice()
                            }
                        }}>
                            Save
                        </button>
                        <button
                            className="btn btn-outline-danger"
                            onClick={() => {
                                this.props.callback()
                            }}
                        >
                            Cancel
                        </button>
                    </div>
                </div>
            </div>
        )
    }
}


class FormOverlay extends Component {
    render () {
        return (
            <Popup trigger={<button className="btn btn-outline-dark">{this.props.label}</button>} modal>
                {close => (
                <Modal>
                    <Content>
                        <Form  label={this.props.label} callback={close} data={this.props.data} update={this.props.update}/>
                    </Content>
                </Modal>
                )}
            </Popup>
        )
    }
}

export default FormOverlay