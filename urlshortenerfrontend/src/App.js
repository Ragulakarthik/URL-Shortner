import React from 'react';
import './App.css'; // Import the CSS file
import ShortenUrl from './components/ShortenUrl';
import UpdateUrl from './components/UpdateUrl';
import GetOriginalUrl from './components/GetOriginalUrl';
import UpdateExpiry from './components/UpdateExpiry';

const App = () => {
    return (
        <div className="app-container">
            <header className="header">
                <h1>URL Shortener</h1>
            </header>
            <div className="main-content">
                <main>
                    <ShortenUrl />
                    <UpdateUrl />
                    <GetOriginalUrl />
                    <UpdateExpiry />
                </main>
            </div>
            <footer className="footer">
                <p>&copy; {new Date().getFullYear()} URL Shortener. All Rights Reserved.</p>
            </footer>
        </div>
    );
};

export default App;
