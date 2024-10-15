import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LandingPage from './components/LandingPage';
import ProfileForm from './components/profileForm';
import MatchPage from "./components/MatchPage";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LandingPage />} />
                <Route path="/register" element={<ProfileForm />} />
                <Route path="/match" element={<MatchPage />} />
            </Routes>
        </Router>
    );
}

export default App;


