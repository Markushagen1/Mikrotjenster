import { useState } from 'react';
import { Home, Users, Building2, ArrowRight } from 'lucide-react';

// LandingPage component
export default function LandingPage() {
    const [hoveredCard, setHoveredCard] = useState<string | null>(null);

    const Card = ({ icon: Icon, title, description, buttonText, type, onMouseEnter, onMouseLeave, className }: any) => {
        const getColors = () => {
            switch (type) {
                case 'register':
                    return 'bg-gradient-to-br from-indigo-500 to-purple-600 hover:from-indigo-600 hover:to-purple-700';
                case 'match':
                    return 'bg-gradient-to-br from-emerald-500 to-teal-600 hover:from-emerald-600 hover:to-teal-700';
                case 'apartment':
                    return 'bg-gradient-to-br from-blue-500 to-cyan-600 hover:from-blue-600 hover:to-cyan-700';
                default:
                    return '';
            }
        };

        return (
            <div
                className={`relative p-8 rounded-xl shadow-lg backdrop-blur-sm bg-white/90 transition-all duration-300 ${
                    hoveredCard === type ? 'transform -translate-y-2 shadow-xl' : ''
                } ${className}`}
                onMouseEnter={onMouseEnter}
                onMouseLeave={onMouseLeave}
            >
                <div className="absolute top-0 right-0 w-24 h-24 bg-gradient-to-br from-purple-100 to-purple-200 rounded-bl-full opacity-20" />
                <div className="mb-4">
                    <Icon className="w-10 h-10 text-purple-600" />
                </div>
                <h3 className="text-xl font-bold mb-3 text-gray-800">{title}</h3>
                <p className="text-gray-600 mb-6 min-h-[60px]">{description}</p>
                <button
                    className={`w-full py-3 px-6 rounded-lg text-white font-medium transition-all duration-300 flex items-center justify-center gap-2 ${getColors()}`}
                >
                    {buttonText}
                    <ArrowRight className="w-4 h-4" />
                </button>
            </div>
        );
    };

    return (
        <div className="min-h-screen bg-gradient-to-b from-purple-50 via-white to-purple-100">
            {/* Navigation */}
            <nav className="container mx-auto px-4 py-6">
                <div className="flex justify-between items-center">
                    <h1 className="text-2xl font-bold text-purple-900">Kollektivet</h1>
                    <div className="space-x-6">
                        <button className="text-purple-900 hover:text-purple-700 transition-colors">Logg inn</button>
                        <button className="bg-purple-600 hover:bg-purple-700 text-white px-4 py-2 rounded-lg transition-colors">
                            Registrer deg
                        </button>
                    </div>
                </div>
            </nav>

            {/* Hero Section */}
            <div className="container mx-auto px-4 py-16">
                <div className="text-center mb-20 max-w-3xl mx-auto">
                    <h1 className="text-6xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-purple-600 to-indigo-600 mb-6">
                        Finn ditt perfekte kollektiv
                    </h1>
                    <p className="text-xl text-gray-600 mb-8 leading-relaxed">
                        La oss hjelpe deg med å finne de beste romkameratene og din drømmebolig.
                        En enklere vei til et fantastisk kollektivliv.
                    </p>
                    <div className="flex gap-4 justify-center">
                        <button className="bg-purple-600 hover:bg-purple-700 text-white px-8 py-3 rounded-lg transition-all duration-300 transform hover:-translate-y-1">
                            Kom i gang nå
                        </button>
                        <button className="bg-white text-purple-600 px-8 py-3 rounded-lg border-2 border-purple-600 hover:bg-purple-50 transition-all duration-300 transform hover:-translate-y-1">
                            Les mer
                        </button>
                    </div>
                </div>

                {/* Card Container */}
                <div className="grid md:grid-cols-3 gap-8 max-w-6xl mx-auto">
                    <Card
                        icon={Users}
                        type="register"
                        title="Registrer deg"
                        description="Start din reise mot å finne det perfekte kollektivet og dine fremtidige romkamerater"
                        buttonText="Kom i gang"
                        onMouseEnter={() => setHoveredCard('register')}
                        onMouseLeave={() => setHoveredCard(null)}
                    />
                    <Card
                        icon={Home}
                        type="match"
                        title="Start Matching"
                        description="Vi hjelper deg å finne personer som passer perfekt med din livsstil og preferanser"
                        buttonText="Finn match"
                        onMouseEnter={() => setHoveredCard('match')}
                        onMouseLeave={() => setHoveredCard(null)}
                    />
                    <Card
                        icon={Building2}
                        type="apartment"
                        title="Registrer Bolig"
                        description="Har du en ledig bolig? Del den med vårt fellesskap av boligsøkere"
                        buttonText="List bolig"
                        onMouseEnter={() => setHoveredCard('apartment')}
                        onMouseLeave={() => setHoveredCard(null)}
                    />
                </div>

                {/* Stats Section */}
                <div className="mt-24 text-center">
                    <h2 className="text-2xl font-bold text-purple-900 mb-8">
                        Norges største plattform for kollektiv-matching
                    </h2>
                    <div className="grid md:grid-cols-3 gap-8 max-w-3xl mx-auto">
                        <div className="bg-white/80 backdrop-blur-sm p-6 rounded-lg shadow-md">
                            <div className="text-3xl font-bold text-purple-600 mb-2">1000+</div>
                            <div className="text-gray-600">Aktive brukere</div>
                        </div>
                        <div className="bg-white/80 backdrop-blur-sm p-6 rounded-lg shadow-md">
                            <div className="text-3xl font-bold text-emerald-600 mb-2">500+</div>
                            <div className="text-gray-600">Vellykkede match</div>
                        </div>
                        <div className="bg-white/80 backdrop-blur-sm p-6 rounded-lg shadow-md">
                            <div className="text-3xl font-bold text-blue-600 mb-2">200+</div>
                            <div className="text-gray-600">Tilgjengelige boliger</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

