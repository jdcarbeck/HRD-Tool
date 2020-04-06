import React, { Component } from 'react'
import { FormList, NewForms, CaseTypeVis, LineFormRate } from '../components'
import api from '../api'

import styled from 'styled-components'

const Card = styled.div.attrs({
    className: 'card',
})`
    margin: 1rem;
    border-style: None;
    border-radius: 5px;
    box-shadow: 0 2px 4px 0 rgba(0,0,0,0.2);
`
const CardTitle = styled.div.attrs({
    className: 'card-header'
})``

const CardWrapper = styled.div`
    margin: auto;
`
class Dashboard extends Component {
    constructor(props){
        super(props)
        this.state = {
            data: {},
            isLoading: true,
        }
    }
    
    componentDidMount = async () => {
        await api.getAllForms().then(forms =>{
            this.setState({
                data: forms.data.data,
                isLoading: false,
            })
        })
    }

    refresh = async () => {
        await api.getAllForms().then(forms =>{
            this.setState({
                data: forms.data.data,
                isLoading: false,
            })
        })
    }

    
    render() {

        const { data } = this.state

        let showDashboard = true
        if(!data.length){
            showDashboard = false
        }

        return (
            <div>
                {showDashboard && (
                    <div>
                        <div className="row">
                            <div className=" col-12">
                                <button className="btn btn-outline-success float-right mr-3" onClick={ this.refresh.bind(this) }>
                                    <span>Reload</span>
                                </button>
                                <button className="btn btn-outline-dark float-right mr-2" onClick={api.download}>Download</button>
                            </div>
                        </div>
                        <div className="row">         
                            <div className="col-lg-12">
                                <Card>
                                    <CardTitle>Reported Incidents</CardTitle>
                                    <CardWrapper>
                                        <div className="card-body">
                                            <LineFormRate data={this.state.data}/>
                                        </div>
                                    </CardWrapper>
                                </Card>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-md-12">
                                <Card>
                                    <CardTitle>Database</CardTitle>
                                    <FormList forms={this.state.data}/>
                                </Card>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        )
    }

}

export default Dashboard