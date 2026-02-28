import React from 'react';
import './EmptyState.css';

const EmptyState = ({ icon = '📭', title = 'Nothing here yet', message, action }) => (
    <div className="empty-state">
        <div className="empty-icon">{icon}</div>
        <h3 className="empty-title">{title}</h3>
        {message && <p className="empty-msg">{message}</p>}
        {action && (
            <button className="empty-action-btn" onClick={action.onClick}>
                {action.label}
            </button>
        )}
    </div>
);

export default EmptyState;
