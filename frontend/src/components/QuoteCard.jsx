import React from 'react';
import './QuoteCard.css';

const QuoteCard = ({ quote }) => {
    if (!quote) return null;
    return (
        <div className="quote-card">
            <span className="quote-mark">"</span>
            <p className="quote-text">{quote.quoteText}</p>
        </div>
    );
};

export default QuoteCard;
