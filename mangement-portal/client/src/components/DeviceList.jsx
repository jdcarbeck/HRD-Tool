import React, { Component } from 'react'
import {
    useTable,
    useFilters,
    useSortBy,
  } from 'react-table'

import styled from 'styled-components'
import  DeviceOverlay  from './DeviceOverlay'
import matchSorter from 'match-sorter'

const Wrapper = styled.div`
  padding: 0;
`

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
            placeholder={`Search ${count} devices...`}
        />
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
    } = useTable (
        {
            columns,
            data,
            defaultColumn,
            filterTypes,
            initialState: {
              sortBy: [{ id: 'name', desc: true }]
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


class DeviceList extends Component {

    newDevice = async () => {
      
    }

    render() {
        const { devices, isLoading } = this.props

        const columns = [
            {
                Header: 'Name',
                accessor: 'name',
                filter: 'fuzzyText',
            },
            {
              Header: 'Assigned Device',
              accessor: 'device_id',
              filter: 'fuzzyText',
            },
            {
                Header: 'Location',
                accessor: 'location',
                filter: 'fuzzyText',
            },
            {
              Header: '',
              accessor: '_id',
              disableFilters: true,
              Cell: function(props) {
                  let data = props.cell.row.original
                  return (
                      <span>
                         <DeviceOverlay label="Edit Device" update={true} data={data}/>
                      </span>
                  )
              },
          },
        ]

        let showTable = true
        if(!devices.length){
            showTable = false
        }


        return (
            <Wrapper>
                {showTable && (
                      <Table
                          data={devices}
                          columns={columns}
                          loading={isLoading}
                      />
                )}
            </Wrapper>
        )
    }
}

export default DeviceList