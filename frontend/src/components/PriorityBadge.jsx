import React from 'react';
import './PriorityBadge.css';

const PriorityBadge = ({ priority }) => {
    const displayPriority = priority || 'MEDIUM';
    const priorityLabel = displayPriority.charAt(0).toUpperCase() + displayPriority.slice(1).toLowerCase();
    const priorityClass = displayPriority.toLowerCase();

    return (
        <span className={`priority-badge ${priorityClass}`}>
            {priorityLabel}
        </span>
    );
};

export default PriorityBadge;
