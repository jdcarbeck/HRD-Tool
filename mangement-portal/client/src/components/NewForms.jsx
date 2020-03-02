import React, { Component } from 'react'
import {
    useTable,
    useSortBy,
} from 'react-table'

import api from '../api'

function Table({ columns, data }) {
    console.log('data on NewForms', data)
    const { 
        getTableProps, 
        getTableBodyProps, 
        headerGroups, 
        rows, 
        prepareRow 
    } = useTable(
        {
            columns,
            data,
        },
        useSortBy
    )
    
    return (
        <table {...getTableProps()}>
            <thead>
                {headerGroups.map(headerGroup => (
                    <tr {...headerGroup.getHeaderGroupProps()}>
                        {headerGroup.headers.map(column => (
                            <th {...column.getHeaderProps(column.getSortByToggleProps())}>
                                {column.render('Header')}
                                <span>
                                    {column.isSorted ? (column.isSortedDesc ? ' 🔽' : ' 🔼') : ''}
                                </span>
                            </th>
                        ))}

                    </tr>
                ))}
            </thead>
            <tbody {...getTableBodyProps()}>
                {rows.map(
                    (row,i) => {
                        prepareRow(row);
                        return (
                            <tr {...row.getRowProps()}>
                                {row.cells.map(cell => {
                                    return <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                                })}
                            </tr>
                        )
                })}
            </tbody>
        </table>

    )
}

class NewForms extends Component {
    render() {
        const { forms, isLoading } = this.props
        console.log('BBBBCL: FormsList -> render -> forms', forms)
        
        const columns = [
            {
                Header: 'ID',
                accessor: '_id',
            },
            {
                Header: 'Time',
                accessor: 'time',
                sortType: 'basic'
            }      
        ]

        let showTable = true
        if(!forms.length){
            showTable=false
        }

        return (
            <Table
                data={forms}
                columns={columns}
                loading={isLoading}
            />
        )
    }
}

export default NewForms