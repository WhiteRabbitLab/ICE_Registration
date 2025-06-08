import React, { useState, useEffect } from 'react';
import { ChevronLeft, Music, User } from 'lucide-react';

// Mock data
const mockArtists = [
  {
    id: 1,
    name: "Luna Rivers",
    photo: "https://images.unsplash.com/photo-1494790108755-2616c96a0d01?w=150&h=150&fit=crop&crop=face",
    trackCount: 24,
    description: "Indie folk singer-songwriter known for ethereal vocals and introspective lyrics that capture the essence of modern wanderlust."
  },
  {
    id: 2,
    name: "The Midnight Collective",
    photo: "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=150&h=150&fit=crop&crop=face",
    trackCount: 18,
    description: "Electronic music duo blending ambient soundscapes with driving beats, creating immersive sonic journeys."
  },
  {
    id: 3,
    name: "Marcus Stone",
    photo: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face",
    trackCount: 32,
    description: "Jazz fusion guitarist whose innovative approach bridges traditional jazz with contemporary rock influences."
  },
  {
    id: 4,
    name: "Aria Chen",
    photo: "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150&h=150&fit=crop&crop=face",
    trackCount: 15,
    description: "Classical crossover violinist bringing orchestral elegance to modern pop arrangements."
  }
];

const mockTracks = {
  1: [
    { id: 1, name: "Whispers in the Wind", genre: "Indie Folk", length: "3:24" },
    { id: 2, name: "Mountain Dreams", genre: "Folk", length: "4:12" },
    { id: 3, name: "City Lights", genre: "Indie Pop", length: "3:45" },
    { id: 4, name: "Solitary Road", genre: "Indie Folk", length: "4:33" }
  ],
  2: [
    { id: 5, name: "Neon Nights", genre: "Electronic", length: "5:18" },
    { id: 6, name: "Digital Dreams", genre: "Ambient", length: "6:42" },
    { id: 7, name: "Pulse", genre: "Techno", length: "4:56" },
    { id: 8, name: "Synthetic Dawn", genre: "Electronic", length: "5:23" }
  ],
  3: [
    { id: 9, name: "Blue Note Variations", genre: "Jazz Fusion", length: "7:15" },
    { id: 10, name: "Electric Groove", genre: "Jazz Rock", length: "5:44" },
    { id: 11, name: "Midnight Serenade", genre: "Jazz", length: "6:28" },
    { id: 12, name: "Fusion Flow", genre: "Jazz Fusion", length: "5:52" }
  ],
  4: [
    { id: 13, name: "Classical Crossroads", genre: "Classical Pop", length: "4:18" },
    { id: 14, name: "String Symphony", genre: "Classical", length: "3:56" },
    { id: 15, name: "Modern Minuet", genre: "Classical Pop", length: "3:32" },
    { id: 16, name: "Violin Variations", genre: "Classical", length: "4:45" }
  ]
};

// Mock API functions
const fetchArtists = () => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(mockArtists), 500);
  });
};

const fetchArtistTracks = (artistId) => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(mockTracks[artistId] || []), 500);
  });
};

// Artist Selection Page Component
const ArtistSelectionPage = ({ onArtistSelect }) => {
  const [artists, setArtists] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadArtists = async () => {
      setLoading(true);
      const artistData = await fetchArtists();
      setArtists(artistData);
      setLoading(false);
    };
    loadArtists();
  }, []);

  if (loading) {
    return (
        <div className="flex items-center justify-center min-h-screen" style={{ backgroundColor: '#0d0d0d' }}>
          <div className="text-lg" style={{ color: '#fdfdfd' }}>Loading artists...</div>
        </div>
    );
  }

  return (
      <div className="max-w-6xl mx-auto p-6">
        {/* Hero Image Section */}
        <div className="mb-6 w-1/2 rounded-lg overflow-hidden shadow-lg">
          <img
              src="https://www.iceservices.com/wp-content/themes/ice/assets/img/iceservices-logo.png"
              alt="ICE Music Services"
              className="w-full h-32 object-contain"
          />
        </div>

        {/* Title Section */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold mb-2" style={{ color: '#fdfdfd' }}>Artists</h1>
        </div>

        <div className="rounded-lg shadow-md overflow-hidden" style={{ backgroundColor: '#fdfdfd' }}>
          <table className="w-full">
            <thead style={{ backgroundColor: '#17a6b1' }}>
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: '#fdfdfd' }}>Artist</th>
              <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: '#fdfdfd' }}>Tracks</th>
              <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: '#fdfdfd' }}>Description</th>
            </tr>
            </thead>
            <tbody className="divide-y" style={{ backgroundColor: '#fdfdfd', borderColor: '#17a6b1' }}>
            {artists.map((artist) => (
                <tr
                    key={artist.id}
                    onClick={() => onArtistSelect(artist)}
                    className="cursor-pointer transition-colors hover:opacity-90"
                    style={{ backgroundColor: '#fdfdfd' }}
                    onMouseEnter={(e) => e.target.closest('tr').style.backgroundColor = '#17a6b1'}
                    onMouseLeave={(e) => e.target.closest('tr').style.backgroundColor = '#fdfdfd'}
                >
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <img
                          className="h-12 w-12 rounded-full object-cover"
                          src={artist.photo}
                          alt={artist.name}
                      />
                      <div className="ml-4">
                        <div className="text-sm font-medium" style={{ color: '#0d0d0d' }}>{artist.name}</div>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center text-sm" style={{ color: '#00699e' }}>
                      <Music className="h-4 w-4 mr-1" />
                      {artist.trackCount} tracks
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="text-sm max-w-md" style={{ color: '#0d0d0d' }}>{artist.description}</div>
                  </td>
                </tr>
            ))}
            </tbody>
          </table>
        </div>
      </div>
  );
};

// Artist View Page Component
const ArtistViewPage = ({ artist, onBack }) => {
  const [tracks, setTracks] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadTracks = async () => {
      setLoading(true);
      const trackData = await fetchArtistTracks(artist.id);
      setTracks(trackData);
      setLoading(false);
    };
    loadTracks();
  }, [artist.id]);

  return (
      <div className="max-w-6xl mx-auto p-6">
        {/* Back button */}
        <button
            onClick={onBack}
            className="flex items-center hover:opacity-80 mb-6 transition-colors"
            style={{ color: '#17a6b1' }}
        >
          <ChevronLeft className="h-5 w-5 mr-1" />
          Back to Artists
        </button>

        {/* Artist Banner */}
        <div className="rounded-lg p-8 mb-8 text-white" style={{ background: 'linear-gradient(to right, #17a6b1, #00699e)' }}>
          <div className="flex items-center">
            <img
                className="h-24 w-24 rounded-full object-cover border-4 border-white"
                src={artist.photo}
                alt={artist.name}
            />
            <div className="ml-6">
              <h1 className="text-4xl font-bold mb-2">{artist.name}</h1>
              <div className="flex items-center text-blue-100 mb-3">
                <Music className="h-5 w-5 mr-2" />
                {artist.trackCount} tracks
              </div>
              <p className="text-blue-100 max-w-2xl">{artist.description}</p>
            </div>
          </div>
        </div>

        {/* Tracks Section */}
        <div className="rounded-lg shadow-md overflow-hidden" style={{ backgroundColor: '#fdfdfd' }}>
          <div className="px-6 py-4 border-b" style={{ backgroundColor: '#17a6b1', borderColor: '#00699e' }}>
            <h2 className="text-xl font-semibold" style={{ color: '#fdfdfd' }}>Tracks</h2>
          </div>

          {loading ? (
              <div className="px-6 py-8 text-center" style={{ color: '#0d0d0d' }}>
                Loading tracks...
              </div>
          ) : tracks.length === 0 ? (
              <div className="px-6 py-8 text-center" style={{ color: '#0d0d0d' }}>
                No tracks available for this artist.
              </div>
          ) : (
              <table className="w-full">
                <thead style={{ backgroundColor: '#17a6b1' }}>
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: '#fdfdfd' }}>Track Name</th>
                  <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: '#fdfdfd' }}>Genre</th>
                  <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: '#fdfdfd' }}>Length</th>
                </tr>
                </thead>
                <tbody className="divide-y" style={{ backgroundColor: '#fdfdfd', borderColor: '#17a6b1' }}>
                {tracks.map((track) => (
                    <tr key={track.id} className="hover:opacity-90"
                        onMouseEnter={(e) => e.target.closest('tr').style.backgroundColor = '#17a6b1'}
                        onMouseLeave={(e) => e.target.closest('tr').style.backgroundColor = '#fdfdfd'}>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm font-medium" style={{ color: '#0d0d0d' }}>{track.name}</div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                    <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                          style={{ backgroundColor: '#a3022d', color: '#fdfdfd' }}>
                      {track.genre}
                    </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm" style={{ color: '#00699e' }}>
                        {track.length}
                      </td>
                    </tr>
                ))}
                </tbody>
              </table>
          )}
        </div>
      </div>
  );
};

// Main App Component
const App = () => {
  const [currentView, setCurrentView] = useState('artists');
  const [selectedArtist, setSelectedArtist] = useState(null);

  const handleArtistSelect = (artist) => {
    setSelectedArtist(artist);
    setCurrentView('artist-detail');
  };

  const handleBackToArtists = () => {
    setCurrentView('artists');
    setSelectedArtist(null);
  };

  return (
      <div className="min-h-screen" style={{ backgroundColor: '#0d0d0d' }}>
        {currentView === 'artists' ? (
            <ArtistSelectionPage onArtistSelect={handleArtistSelect} />
        ) : (
            <ArtistViewPage artist={selectedArtist} onBack={handleBackToArtists} />
        )}
      </div>
  );
};

export default App;