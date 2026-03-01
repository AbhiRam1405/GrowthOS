import React from 'react';
import PriorityBadge from './PriorityBadge';
import './TaskCard.css';

const categoryColors = {
    Health: '#10b981',
    Coding: '#4361ee',
    Interview: '#f59e0b',
    Reading: '#8b5cf6',
    Fitness: '#ef4444',
    Learning: '#06b6d4',
    Other: '#6b7280',
};

const TaskCard = ({ task, onToggle }) => {
    const color = categoryColors[task.category] || categoryColors.Other;
    const isGloballyCompleted = task.status === 'COMPLETED';

    return (
        <div className={`task-card ${task.completed ? 'completed' : ''}`}
            style={{ '--cat-color': color }}>
            <label className="task-checkbox-label">
                <input
                    type="checkbox"
                    checked={task.completed}
                    onChange={() => onToggle(task.taskId, !task.completed)}
                    className="task-checkbox"
                    disabled={isGloballyCompleted}
                />
                <span className="custom-checkbox" />
            </label>
            <div className="task-info">
                <span className="task-title">{task.title}</span>
                <div className="task-meta">
                    <span className="task-badge category-badge"
                        style={{ background: color + '20', color }}>
                        {task.category}
                    </span>
                    <span className="task-badge freq-badge">{task.frequency}</span>
                    <PriorityBadge priority={task.priority} />
                </div>
            </div>
        </div>
    );
};

export default TaskCard;
