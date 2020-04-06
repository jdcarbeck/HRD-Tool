import React, { Component } from 'react'
import { DeviceList, DeviceOverlay } from '../components'
import styled from 'styled-components'
import api from '../api'


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


class Devices extends Component {
    constructor(props){
        super(props)
        this.state = {
            data: {},
            isLoading: true,
        }
    }
    
    componentDidMount = async () => {
        await api.getAllDevices().then(devices =>{
            this.setState({
                data: devices.data.data,
                isLoading: false,
            })
        })
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


    render() {
        const { data } = this.state

        let showDevices = true
        if(!data.length){
            showDevices = false
        }

        return (
            <div>
                {showDevices && (
                    <div className="container">
                        <div className="row">
                            <div className="col-3 ml-3">
                                <DeviceOverlay label="New Device" update={false} data={{}}/>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-md-12">
                                <Card>
                                    <CardTitle>Devices</CardTitle>
                                    <DeviceList devices={this.state.data}/>
                                </Card>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        )
    }

}

export default Devices