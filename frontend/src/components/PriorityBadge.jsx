import React from 'react';
import './PriorityBadge.css';

const PriorityBadge = ({ priority }) => {
    const priorityLabel = priority?.charAt(0).toUpperCase() + priority?.slice(1).toLowerCase() || 'Medium';
    const priorityClass = priority?.toLowerCase() || 'medium';

    return (
        <span className={`priority-badge ${priorityClass}`}>
            {priorityLabel}
        </span>
    );
};

export default PriorityBadge;
