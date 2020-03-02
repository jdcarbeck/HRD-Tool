import React, { Component } from 'react'
import '../../node_modules/react-vis/dist/style.css'
import { RadialChart } from 'react-vis'

class CaseTypeVis extends Component {
    render() {
        const data = [{angle: 7, label: 'One'}, {angle: 5, label: 'Two'}, {angle:7, label: 'Three'}]
    
        return (
            <RadialChart
                data={data}
                width={250}
                height={250}
                showLabels={true}
                labelsRadiusMultiplier={0.75}
                innerRadius={1}
            />
        )
    }
}

export default CaseTypeVis