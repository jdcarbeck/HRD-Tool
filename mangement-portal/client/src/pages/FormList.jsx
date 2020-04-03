import React, { Component } from 'react'
import ReactTable from 'react-table'
import api from '../api'

import styled from 'styled-components'

import 'react-table/react-table.css'

const Wrapper = styled.div`
    padding: 0 40px 40px 40px;
`

class FormList extends Component {
    constructor(props){
        super(props)
        this.state= {
            forms: [],
            columns: [],
            isLoading: false,
        }
    }

    componentDidMount = async () => {
        this.setState({ isLoading: true })

        await api.getAllForms().then(froms =>{
            this.setState({
                forms: forms.data.data,
                isLoading: false,
            })
        })
    }

    render() {
        const { forms, isLoading } = this.state
        console.log('TCL: FormsList -> render -> forms', forms)

        const columns = [
            {
                Header: 'ID',
                accessor: '_id',
                filterable: true,
            },
            {
                Header: 'Type',
                accessor: 'type',
                filterable: true,
            },
            {   
                Header: 'Time',
                accessor: 'time',
                Cell: props => <span>{props.value.join(' / ')}</span>,
            }
        ]

        let showTable = true
        if(!forms.length){
            showTable = false
        }


        return (
            <Wrapper>
                {showTable && (
                    <ReactTable
                        data={forms}
                        columns={columns}
                        loading={isLoading}
                        defaultPageSize={10}
                        showPageSizeOptions={true}
                        minRows={0}
                    />
                )}
            </Wrapper>
        )
    }
}

export default FormList