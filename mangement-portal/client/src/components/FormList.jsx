import React, { Component } from 'react'
import {
    useTable,
    useFilters,
    useSortBy,
    useGlobalFilter,
    usePagination,
  } from 'react-table'

import styled from 'styled-components'
import  FormOverlay  from './FormOverlay'
import matchSorter from 'match-sorter'
import api from '../api';

const Wrapper = styled.div`
  padding: 0;
`
const Abuse = styled.span`
  margin-right: 5px;
`

// UI global filter
function GlobalFilter({
    preGlobalFilteredRows,
    globalFilter,
    setGlobalFilter,
}) {
    const count = preGlobalFilteredRows.length

    return (
        <span>
            <input
                value={globalFilter || ''}
                onChange={ e => {
                    setGlobalFilter(e.target.value || undefined) //set or remove a filter
                }}
                placeholder={`${count} forms...`}
                style={{
                    fontSize: '1.1rem',
                    border: '0',
                }}
            />
        </span>
    )
}

//filter UI
function DefaultColumnFilter({
    column: {filterValue, preFilteredRows, setFilter },
}) {
    const count = preFilteredRows.length

    return (
        <input
            value={filterValue || ''}
            onChange={e => {
                setFilter(e.target.value || undefined)
            }}
            placeholder={`Search ${count} forms...`}
        />
    )
}

function NumberRangeColumnFilter({
    column: { filterValue = [], preFilteredRows, setFilter, id },
  }) {
    const [min, max] = React.useMemo(() => {
      let min = preFilteredRows.length ? preFilteredRows[0].values[id] : 0
      let max = preFilteredRows.length ? preFilteredRows[0].values[id] : 0
      preFilteredRows.forEach(row => {
        min = Math.min(row.values[id], min)
        max = Math.max(row.values[id], max)
      })
      return [min, max]
    }, [id, preFilteredRows])
  
    return (
      <div
        style={{
          display: 'flex',
        }}
      >
        <input
          value={filterValue[0] || ''}
          type="number"
          onChange={e => {
            const val = e.target.value
            setFilter((old = []) => [val ? parseInt(val, 10) : undefined, old[1]])
          }}
          placeholder={`Min (${min})`}
          style={{
            width: '70px',
            marginRight: '0.5rem',
          }}
        />
        to
        <input
          value={filterValue[1] || ''}
          type="number"
          onChange={e => {
            const val = e.target.value
            setFilter((old = []) => [old[0], val ? parseInt(val, 10) : undefined])
          }}
          placeholder={`Max (${max})`}
          style={{
            width: '70px',
            marginLeft: '0.5rem',
          }}
        />
      </div>
    )
  }

// This is a custom filter UI for selecting
// a unique option from a list
function SelectColumnFilter({
    column: { filterValue, setFilter, preFilteredRows, id },
  }) {
    // Calculate the options for filtering
    // using the preFilteredRows
    const options = React.useMemo(() => {
      const options = new Set()
      preFilteredRows.forEach(row => {
        options.add(row.values[id])
      })
      return [...options.values()]
    }, [id, preFilteredRows])
  
    // Render a multi-select box
    return (
      <select
        value={filterValue}
        onChange={e => {
          setFilter(e.target.value || undefined)
        }}
      >
        <option value="">All</option>
        {options.map((option, i) => (
          <option key={i} value={option}>
            {option}
          </option>
        ))}
      </select>
    )
  }

  function fuzzyTextFilterFn(rows, id, filterValue) {
    return matchSorter(rows, filterValue, { keys: [row => row.values[id]] })
  }
  
  // Let the table remove the filter if the string is empty
  fuzzyTextFilterFn.autoRemove = val => !val  

//This is the table component
function Table({ columns, data }) {
    console.log('data on FormList', data)
    const filterTypes = React.useMemo(
        () => ({
            text: (rows, id, filterValue) => {
                return rows.filter(row => {
                    const rowValue = row.values[id]
                    return rowValue !== undefined
                        ? String(rowValue)
                            .toLowerCase()
                            .startsWith(String(filterValue).toLowerCase())
                        : true
                })
            },
        }),
        []
    )

    const defaultColumn = React.useMemo(
        () => ({
            Filter: DefaultColumnFilter,
        }),
        []
    )

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        rows,
        prepareRow,
        state,
        visibleColumns,
        preGlobalFilteredRows,
        setGlobalFilter,
    } = useTable (
        {
            columns,
            data,
            defaultColumn,
            filterTypes,
            initialState: {
              sortBy: [{ id: 'incident_date', desc: true }]
            }
        },
        useFilters,
        useSortBy,
    )

    const firstPageRows = rows.slice(0,10)

    return (
        <>
          <table className="table" {...getTableProps()}>
            <thead>
              {headerGroups.map(headerGroup => (
                <tr {...headerGroup.getHeaderGroupProps()}>
                  {headerGroup.headers.map(column => (
                    <th scope="col" {...column.getHeaderProps(column.getSortByToggleProps())}>
                      {column.render('Header')}
                      {/* Render the columns filter UI */}
                      <div>{column.canFilter ? column.render('Filter') : null}</div>
                      <span>{column.isSorted ? (column.isSortedDesc ? '' : '') : ''}</span>
                    </th>
                  ))}
                </tr>
              ))}
            </thead>
            <tbody {...getTableBodyProps()}>
              {firstPageRows.map((row, i) => {
                prepareRow(row)
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
          <br />
          <div>Showing the first 20 results of {rows.length} rows</div>
        </>
    )
}

function filterGreaterThan(rows, id, filterValue) {
    return rows.filter(row => {
        const rowValue = row.values[id]
        return rowValue >= filterValue
    })
}

filterGreaterThan.autoRemove = val => typeof val !== 'number'

class ViewForm extends Component {
  render(){
    return <button className="btn btn-outline-info">View</button>
  }
}


class FormList extends Component {

    render() {
        const { forms, isLoading } = this.props
        console.log('TCL: FormsList -> render -> forms', forms)

        const columns = [
            {   
                Header: 'Incident Date',
                accessor: 'incident_date',
                sortType: 'basic',
                Cell: function(cellProps){
                  let date = new Date(cellProps.cell.value)
                  var mm = date.getMonth() + 1; // getMonth() is zero-based
                  var dd = date.getDate();
                  var fulldate = [(dd>9 ? '' : '0') + dd, (mm>9 ? '' : '0') + mm, date.getFullYear()].join('/');
                  return(
                    <span>{fulldate}</span>
                  )
                },
            },
            {
                Header: 'Community',
                accessor: 'community',
                filter: 'fuzzyText',
            },
            {
              Header: 'Age',
              accessor: 'age_range',
              Filter: SelectColumnFilter,
              filter: 'includes'
            },
            {
                Header: 'Victim Gender',
                accessor: 'gender',
                Filter: SelectColumnFilter,
                filter: 'includes'
            },
            {
              Header: 'Relation',
              accessor: 'perpetrator_association',
              Filter: SelectColumnFilter,
              filter: 'includes'
            },
            {
              Header: 'Abuse',
              accessor: ' ',
              disableFilters: true,
              Cell: function(cellProps){
                console.log(cellProps)
                let data = cellProps.cell.row.original
                let sexual = data.sexual_abuse
                let emotional = data.emotional_abuse
                let physical = data.physical_abuse
                return (
                  <span>
                    <Abuse className="badge badge-warning">{(sexual ? 'sexual' : '')}</Abuse>
                    <Abuse className="badge badge-warning">{(emotional ? 'emotional' : '')}</Abuse>
                    <Abuse className="badge badge-warning">{(physical ? 'physical' : '')}</Abuse>
                  </span>
                )
              }
            },
            {
              Header: '',
              accessor: '_id',
              disableFilters: true,
              Cell: function(props) {
                  let data = props.cell.row.original
                  return (
                      <span>
                          <FormOverlay data={data}/>
                      </span>
                  )
              },
          },
        ]

        let showTable = true
        if(!forms.length){
            showTable = false
        }


        return (
            <Wrapper>
                {showTable && (
                      <Table
                          data={forms}
                          columns={columns}
                          loading={isLoading}
                      />
                )}
            </Wrapper>
        )
    }
}

export default FormList