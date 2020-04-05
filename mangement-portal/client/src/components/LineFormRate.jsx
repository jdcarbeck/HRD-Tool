import React, {Component} from 'react'
import '../../node_modules/react-vis/dist/style.css'
import { XYPlot, XAxis, YAxis, VerticalGridLines,
HorizontalGridLines, LineSeries, LineMarkSeries } from 'react-vis'
var moment = require('moment');
var twix = require('twix');

function getDateFromStr(dateStr){
    let date = new Date(dateStr)
    var mm = date.getMonth() + 1; // getMonth() is zero-based
    var dd = date.getDate();
    return [(mm>9 ? '' : '0') + mm, (dd>9 ? '' : '0') + dd, date.getFullYear()].join('-');
}

//need to do something so its for the last 30 days
function PreProcess(data){

    var processed_data = {}
    var graph_data = []

    var today = new Date()
    var pastDate = today.getDate() - 30;
    today.setDate(pastDate);

    for(let i = 0; i < 31; i++){
        var dateStr = today.toISOString()
        dateStr = getDateFromStr(dateStr)
        processed_data[new Date(dateStr)] = 0;
        var nextDate = today.getDate() + 1;
        today.setDate(nextDate);
    }

    console.log("Processed data");
    console.log(processed_data)

    for(let i = 0; i < data.length; i++){
        var dateStr = getDateFromStr(data[i].incident_date)
        var key = new Date(dateStr)
        var count = processed_data[key]
        processed_data[key] = count + 1
    }

    console.log(processed_data)

    Object.keys(processed_data).forEach((key) => {
        graph_data.push({"x": new Date(key), "y": processed_data[key]})
    })

    console.log(graph_data)
    return graph_data
}

class LineFormRate extends Component {
    render() {
        const data = PreProcess(this.props.data)
        console.log(data)
        return (
            <XYPlot
                height={250}
                width={900}
                xType="time"
                >
                <VerticalGridLines />
                <HorizontalGridLines />
                <XAxis title="Days" />
                <YAxis title="Incidents"/>
                <LineSeries
                    data={data}
                    curve={'curveMonotoneX'}
                    style={{
                        stroke: '#0081CC', 
                        strokeWidth: 3,
                        }} />
            </XYPlot>
            
        )
    }
}

export default LineFormRate