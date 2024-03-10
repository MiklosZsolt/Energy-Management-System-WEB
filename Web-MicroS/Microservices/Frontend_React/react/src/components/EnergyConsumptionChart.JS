import React from 'react';
import { Bar } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
  } from 'chart.js';
  
  ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
  );

const EnergyConsumptionChart = ({ data }) => {
  const chartData = {
    labels: data.map((measurement) => measurement.hour), // Assuming 'hour' is the label
    datasets: [{
      label: 'Energy Consumption (kWh)',
      data: data.map((measurement) => measurement.energyConsumed),
      backgroundColor: 'rgba(0, 123, 255, 0.5)', // Example color
    }]
  };

  const options = {
    scales: {
      yAxes: [{
        ticks: {
          beginAtZero: true
        }
      }]
    }
  };

  return <Bar data={chartData} options={options} />;
};

export default EnergyConsumptionChart;
