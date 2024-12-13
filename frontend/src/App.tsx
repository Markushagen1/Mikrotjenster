import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LandingPage from './components/LandingPage';
import ProfileForm from './components/profileForm';
import MatchPage from "./components/MatchPage";
import MatchesList from "./components/MatchesList";
import Login from "./components/Login";
import Register from "./components/Register";
import ChatPage from "./components/ChatPage";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LandingPage />} />
                <Route path="/register" element={<ProfileForm />} />
                <Route path="/match" element={<MatchPage />} />
                <Route path="/matches" element={<MatchesList />} />
                <Route path="/login" element={<Login />} />
                <Route path="/regi" element={<Register />} />
                <Route path="/chat/:matchId" element={<ChatPage />} />
            </Routes>
        </Router>
    );
}

export default App;


