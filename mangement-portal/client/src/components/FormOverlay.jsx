import React, { Component } from 'react'
import styled from 'styled-components'
import Popup from 'reactjs-popup'

const Modal = styled.div`
margin-bottom: 20px;
background-color: #FFFFFF;
border-radius: 5px;
`
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

function ClaimList(props) {
    const claims = props.claims;
    const listItems = claims.map((claim) =>
      <li>
        {claim}
      </li>
    );
    return (
      <ul>{listItems}</ul>
    );
}

function getDateFromStr(dateStr){
    let date = new Date(dateStr)
    var mm = date.getMonth() + 1; // getMonth() is zero-based
    var dd = date.getDate();
    return [(dd>9 ? '' : '0') + dd, (mm>9 ? '' : '0') + mm, date.getFullYear()].join('/');
}


class Form extends Component {
    render() {
        let incident_date = getDateFromStr(this.props.data.incident_date)
        let attention_date = getDateFromStr(this.props.data.attention_date)
        return (
            <div className="container">
                <div className="row">
                    <div className="col-12">
                        <h4>Form ID: <span className="badge badge badge-secondary">{this.props.data._id}</span></h4>
                    </div>
                </div>
                <div className="row">
                    <div className="col-6">
                        <h5 className="float-center"><strong>Incident Date: </strong>{incident_date}</h5>
                    </div>
                    <div className="col-6">  
                        <h5 className="float-center"><strong>Attention Date: </strong>{attention_date}</h5>
                    </div>
                </div>   
                <hr></hr>      
                <div className="row">
                    <div className="col-6" > 
                        <h5>Victim Info:</h5>
                        <ul>
                            <li>Age: <span className="badge badge badge-light">{this.props.data.age_range}</span></li>
                            <li>Gender: <span className="badge badge badge-light">{this.props.data.gender}</span></li>
                            <li>Location: <span className="badge badge badge-light">{this.props.data.community}</span></li>
                        </ul>
                    </div>
                    <div className="col-6">
                        <h5>Abuser:</h5>
                        <ul>
                            <li>Gender: <span className="badge badge badge-light">{this.props.data.perpetrator_gender}</span></li>
                            <li>Relation to Victim:  <span className="badge badge badge-light">{this.props.data.perpetrator_association}</span></li>
                        </ul>
                    </div>
                </div>
                <hr/>
                <div className="row">
                    <h5>Claim</h5>
                </div>
                <div className="row">
                    <div className="col-12">
                        <div className="row">
                            <div className={this.props.data.physical_abuse ? 'col-4' : 'd-none'}>
                                <strong>Physical Abuse:</strong>
                                <ClaimList claims={this.props.data.physical_abuse_suffered}/>
                            </div>
                            <div className={this.props.data.emotional_abuse ? 'col-4' : 'd-none'}>
                                <strong>Emotional Abuse:</strong>
                                <ClaimList claims={this.props.data.emotional_abuse_suffered}/>
                            </div>
                            <div className={this.props.data.sexual_abuse ? 'col-4' : 'd-none'}>
                                <strong>Sexual Abuse:</strong>
                                <ClaimList claims={this.props.data.sexual_abuse_suffered}/>
                            </div>
                        </div>
                        
                    </div>
                </div>
                <hr/>
                <div className="row">
                    <h5>Support</h5>
                </div>
                <div className="row">
                    <div className="col-4">
                        <strong>Support sought:</strong>
                        <ClaimList claims={this.props.data.support_sought}/>
                    </div>
                    <div className="col-4">
                        <strong>Support offered:</strong>
                        <ClaimList claims={this.props.data.support_offered}/>
                    </div>
                    <div className="col-4">
                        <strong>Referred to:</strong>
                        <ClaimList claims={this.props.data.support_referred}/>
                    </div>
                </div>
            </div>
        )
    }
}

{/* <Popup
    trigger={<button className="btn btn-outline-primary"> Trigger </button>}
    position="top center"
    closeOnDocumentClick>
    <span>
        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Beatae
        magni omnis delectus nemo, maxime molestiae dolorem numquam
        mollitia, voluptate ea, accusamus excepturi deleniti ratione
        sapiente! Laudantium, aperiam doloribus. Odit, aut.
    </span>
</Popup> */}


class FormOverlay extends Component {
    render () {
        return (
            <Popup trigger={<button className="btn btn-outline-success">View</button>} modal>
                {close => (
                <Modal>
                    <Content>
                        <Form data={this.props.data}/>
                    </Content>
                    <Actions>
                        <button
                            className="btn btn-outline-secondary"
                            onClick={() => {
                            console.log("form closed ");
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

export default FormOverlay