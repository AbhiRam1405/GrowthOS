import React from 'react';
import './SummaryCard.css';

const SummaryCard = ({ icon, label, value, sub, color = '#4361ee' }) => (
    <div className="summary-card" style={{ '--accent': color }}>
        <div className="summary-icon">{icon}</div>
        <div className="summary-content">
            <p className="summary-label">{label}</p>
            <p className="summary-value">{value}</p>
            {sub && <p className="summary-sub">{sub}</p>}
        </div>
    </div>
);

export default SummaryCard;
