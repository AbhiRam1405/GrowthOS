import React from 'react';
import './LoadingSpinner.css';

const LoadingSpinner = ({ message = 'Loading...' }) => (
    <div className="spinner-wrapper">
        <div className="spinner" />
        <p className="spinner-msg">{message}</p>
    </div>
);

export default LoadingSpinner;
