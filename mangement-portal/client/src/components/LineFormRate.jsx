import React, {Component} from 'react'
import '../../node_modules/react-vis/dist/style.css'
import { XYPlot, XAxis, YAxis, VerticalGridLines,
HorizontalGridLines, LineSeries } from 'react-vis'

class LineFormRate extends Component {
    render() {
        const data = [{x: 2014, y: 4}, {x:2015, y:10},{x:2016, y:12},{x:2017,y:6}]

        return (
            <XYPlot
                height={250}
                width={900}
                >
                <VerticalGridLines />
                <HorizontalGridLines />
                <XAxis title="Days" />
                <YAxis title="Form Submissions"/>
                <LineSeries
                    data={data}
                    style={{stroke: 'blue', strokeWidth: 3}} />
            </XYPlot>
            
        )
    }
}

export default LineFormRate