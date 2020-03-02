import React, { Component } from 'react'
import {
    useTable,
    useSortBy,
} from 'react-table'

import styled from 'styled-components'

const Styles = styled.div`
    padding: 1rem;

    table {
        border-spacing: 0;
        border: 1px solid black;

        tr{
            :last-child {
                td {
                    border-bottom: 0;
                }
            }
        }

        th,
        td {
            margin: 0;
            padding: 0.5rem;
            border-bottom: 1px solid black;
            border-right: 1px solid black;

            :last-child {
                border-right: 0;
            }
        }
    }
`

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
        const { forms } = this.props
        
        const columns = [
            {
                Header: 'ID',
                accessor: '_id',
            },
            {
                Header: 'Date',
                accessor: 'time',
                sortType: 'basic',
                Cell: function(cellProps){
                    console.log(cellProps)
                    return(
                        <span>{cellProps.cell.value.join('/')}</span>
                    )
                },
            }      
        ]

        return (
            <Styles>
                <Table
                    data={forms}
                    columns={columns}
                />
            </Styles>
        )
    }
}

export default NewForms