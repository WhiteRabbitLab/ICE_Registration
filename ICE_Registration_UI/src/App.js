import React, { useState, useEffect } from 'react';
import { ChevronLeft, Music, User } from 'lucide-react';

// API configuration
const API_BASE_URL = 'http://localhost:8080/api';

// API functions
const fetchArtists = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}/artists`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching artists:', error);
    throw error;
  }
};

const fetchArtistById = async (artistId) => {
  try {
    const response = await fetch(`${API_BASE_URL}/artists/${artistId}`);
    if (!response.ok) {
      if (response.status === 404) {
        throw new Error('Artist not found');
      }
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching artist:', error);
    throw error;
  }
};

// Artist Selection Page Component
const ArtistSelectionPage = ({ onArtistSelect }) => {
  const [artists, setArtists] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadArtists = async () => {
      try {
        setLoading(true);
        setError(null);
        const artistData = await fetchArtists();
        setArtists(artistData);
      } catch (err) {
        setError('Failed to load artists. Please try again later.');
        console.error('Error loading artists:', err);
      } finally {
        setLoading(false);
      }
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

  if (error) {
    return (
        <div className="flex items-center justify-center min-h-screen" style={{ backgroundColor: '#0d0d0d' }}>
          <div className="text-center">
            <div className="text-lg mb-4" style={{ color: '#a3022d' }}>{error}</div>
            <button
                onClick={() => window.location.reload()}
                className="px-4 py-2 rounded text-white"
                style={{ backgroundColor: '#17a6b1' }}
            >
              Retry
            </button>
          </div>
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

        {artists.length === 0 ? (
            <div className="text-center py-8" style={{ color: '#fdfdfd' }}>
              No artists found.
            </div>
        ) : (
            <div className="rounded-lg shadow-md overflow-hidden" style={{ backgroundColor: '#fdfdfd' }}>
              <table className="w-full">
                <thead style={{ backgroundColor: '#17a6b1' }}>
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: '#fdfdfd' }}>Artist</th>
                  <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: '#fdfdfd' }}>Track Count</th>
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
                          {artist.photo ? (
                              <img
                                  className="h-12 w-12 rounded-full object-cover"
                                  src={artist.photo}
                                  alt={artist.name}
                                  onError={(e) => {
                                    e.target.style.display = 'none';
                                    e.target.nextSibling.style.display = 'flex';
                                  }}
                              />
                          ) : null}
                          <div
                              className="h-12 w-12 rounded-full flex items-center justify-center mr-4"
                              style={{
                                backgroundColor: '#17a6b1',
                                display: artist.photo ? 'none' : 'flex'
                              }}
                          >
                            <User className="h-6 w-6" style={{ color: '#fdfdfd' }} />
                          </div>
                          <div className="ml-4">
                            <div className="text-sm font-medium" style={{ color: '#0d0d0d' }}>{artist.name}</div>
                          </div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center text-sm" style={{ color: '#00699e' }}>
                          <Music className="h-4 w-4 mr-1" />
                          {artist.trackCount || 0} tracks
                        </div>
                      </td>
                      <td className="px-6 py-4">
                        <div className="text-sm max-w-md" style={{ color: '#0d0d0d' }}>
                          {artist.description || 'No description available'}
                        </div>
                      </td>
                    </tr>
                ))}
                </tbody>
              </table>
            </div>
        )}
      </div>
  );
};

// Artist View Page Component
const ArtistViewPage = ({ artist, onBack }) => {
  const [artistDetails, setArtistDetails] = useState(artist);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadArtistDetails = async () => {
      if (!artist.id) return;

      try {
        setLoading(true);
        setError(null);
        const detailData = await fetchArtistById(artist.id);
        setArtistDetails(detailData);
      } catch (err) {
        setError('Failed to load artist details.');
        console.error('Error loading artist details:', err);
      } finally {
        setLoading(false);
      }
    };

    loadArtistDetails();
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
            {artistDetails.photo ? (
                <img
                    className="h-24 w-24 rounded-full object-cover border-4 border-white"
                    src={artistDetails.photo}
                    alt={artistDetails.name}
                    onError={(e) => {
                      e.target.style.display = 'none';
                      e.target.nextSibling.style.display = 'flex';
                    }}
                />
            ) : null}
            <div
                className="h-24 w-24 rounded-full border-4 border-white flex items-center justify-center mr-6"
                style={{
                  backgroundColor: 'rgba(255,255,255,0.2)',
                  display: artistDetails.photo ? 'none' : 'flex'
                }}
            >
              <User className="h-12 w-12 text-white" />
            </div>
            <div className="ml-6">
              <h1 className="text-4xl font-bold mb-2">{artistDetails.name}</h1>
              <div className="flex items-center text-blue-100 mb-3">
                <Music className="h-5 w-5 mr-2" />
                {artistDetails.trackCount || 0} tracks
              </div>
              <p className="text-blue-100 max-w-2xl">
                {artistDetails.description || 'No description available'}
              </p>
            </div>
          </div>
        </div>

        {/* Artist Details Section */}
        <div className="rounded-lg shadow-md overflow-hidden" style={{ backgroundColor: '#fdfdfd' }}>
          <div className="px-6 py-4 border-b" style={{ backgroundColor: '#17a6b1', borderColor: '#00699e' }}>
            <h2 className="text-xl font-semibold" style={{ color: '#fdfdfd' }}>Artist Information</h2>
          </div>

          {loading ? (
              <div className="px-6 py-8 text-center" style={{ color: '#0d0d0d' }}>
                Loading artist details...
              </div>
          ) : error ? (
              <div className="px-6 py-8 text-center" style={{ color: '#a3022d' }}>
                {error}
              </div>
          ) : (
              <div className="px-6 py-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <h3 className="text-lg font-semibold mb-2" style={{ color: '#0d0d0d' }}>Details</h3>
                    <div className="space-y-2">
                      <div className="flex justify-between">
                        <span style={{ color: '#00699e' }}>Artist ID:</span>
                        <span style={{ color: '#0d0d0d' }}>{artistDetails.id}</span>
                      </div>
                      <div className="flex justify-between">
                        <span style={{ color: '#00699e' }}>Name:</span>
                        <span style={{ color: '#0d0d0d' }}>{artistDetails.name}</span>
                      </div>
                      <div className="flex justify-between">
                        <span style={{ color: '#00699e' }}>Track Count:</span>
                        <span style={{ color: '#0d0d0d' }}>{artistDetails.trackCount || 0}</span>
                      </div>
                    </div>
                  </div>
                  <div>
                    <h3 className="text-lg font-semibold mb-2" style={{ color: '#0d0d0d' }}>Description</h3>
                    <p style={{ color: '#0d0d0d' }}>
                      {artistDetails.description || 'No description available for this artist.'}
                    </p>
                  </div>
                </div>
              </div>
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