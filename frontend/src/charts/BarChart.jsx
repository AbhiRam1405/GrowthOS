import React from 'react';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';
import { Bar } from 'react-chartjs-2';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const PALETTE = ['#4361ee', '#10b981', '#f59e0b', '#8b5cf6', '#ef4444', '#06b6d4', '#6b7280'];

const BarChart = ({ data = {} }) => {
    const labels = Object.keys(data);
    const values = Object.values(data);

    const chartData = {
        labels,
        datasets: [
            {
                label: 'Completions',
                data: values,
                backgroundColor: labels.map((_, i) => PALETTE[i % PALETTE.length] + 'cc'),
                borderColor: labels.map((_, i) => PALETTE[i % PALETTE.length]),
                borderWidth: 2,
                borderRadius: 8,
                borderSkipped: false,
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
                    label: ctx => ` ${ctx.parsed.y} completions`,
                },
            },
        },
        scales: {
            y: {
                beginAtZero: true,
                ticks: {
                    stepSize: 1,
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
            <Bar data={chartData} options={options} />
        </div>
    );
};

export default BarChart;
