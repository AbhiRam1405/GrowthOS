import React from 'react';
import './ErrorMessage.css';

const ErrorMessage = ({ message, onRetry }) => (
    <div className="error-card">
        <span className="error-icon">⚠️</span>
        <p className="error-text">{message || 'Something went wrong. Please try again.'}</p>
        {onRetry && (
            <button className="retry-btn" onClick={onRetry}>
                Try Again
            </button>
        )}
    </div>
);

export default ErrorMessage;
