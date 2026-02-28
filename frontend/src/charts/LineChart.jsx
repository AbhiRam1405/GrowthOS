import React from 'react';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    Filler,
} from 'chart.js';
import { Line } from 'react-chartjs-2';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend, Filler);

const LineChart = ({ data = [] }) => {
    const labels = data.map(d => {
        const date = new Date(d.date);
        return date.toLocaleDateString('en-IN', { month: 'short', day: 'numeric' });
    });
    const values = data.map(d => d.completionPercentage);

    const chartData = {
        labels,
        datasets: [
            {
                label: 'Completion %',
                data: values,
                borderColor: '#4361ee',
                backgroundColor: 'rgba(67, 97, 238, 0.1)',
                pointBackgroundColor: '#4361ee',
                pointBorderColor: '#fff',
                pointBorderWidth: 2,
                pointRadius: 5,
                pointHoverRadius: 7,
                tension: 0.4,
                fill: true,
            },
        ],
    };

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { display: false },
            tooltip: {
                callbacks: {
                    label: ctx => ` ${ctx.parsed.y.toFixed(1)}%`,
                },
            },
        },
        scales: {
            y: {
                min: 0,
                max: 100,
                ticks: {
                    callback: v => `${v}%`,
                    font: { size: 11 },
                    color: '#7a8099',
                },
                grid: { color: '#f0f4ff' },
            },
            x: {
                ticks: { font: { size: 11 }, color: '#7a8099' },
                grid: { display: false },
            },
        },
    };

    return (
        <div style={{ height: '260px' }}>
            <Line data={chartData} options={options} />
        </div>
    );
};

export default LineChart;
