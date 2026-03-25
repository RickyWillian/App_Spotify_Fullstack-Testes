import React, { useState } from 'react';
import axios from 'axios';
import './App.css'; 

function App() {
  const [searchTerm, setSearchTerm] = useState('');
  const [tracks, setTracks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [message, setMessage] = useState(null); // Novo estado para mensagens de sucesso/erro

  const handleSearch = async () => {
    // Limpa a mensagem anterior ao iniciar uma nova busca
    setMessage(null);
    if (searchTerm.trim() === '') {
      setError("O termo de busca não pode ser vazio.");
      return;
    }

    setLoading(true);
    setError(null);
    setTracks([]);

    try {
      const response = await axios.get(`http://localhost:8888/api/search?query=${encodeURIComponent(searchTerm)}`);
      setTracks(response.data);
    } catch (err) {
      console.error("Erro ao buscar músicas:", err);
      if (err.response && err.response.status === 400) {
        setError(err.response.data);
      } else {
        setError("Não foi possível buscar as músicas. Verifique a conexão com o backend.");
      }
    } finally {
      setLoading(false);
    }
  };

  const handleAddToQueue = async (uri) => {
    // Limpa a mensagem anterior ao iniciar uma nova tentativa
    setMessage(null);
    try {
      const response = await axios.post(`http://localhost:8888/api/add-to-queue?uri=${encodeURIComponent(uri)}`);
      setMessage(response.data); // Armazena a mensagem de sucesso
    } catch (err) {
      console.error("Erro ao adicionar música à fila:", err);
      if (err.response) {
        setMessage(err.response.data); // Armazena a mensagem de erro
      } else if (err.request) {
        setMessage("Erro de conexão. Verifique se o backend está rodando.");
      } else {
        setMessage("Erro ao adicionar a música. Tente novamente.");
      }
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Jukebox Spotify</h1>
        <div className="search-container">
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Buscar por músicas..."
			data-cy="search-input"
          />
          <button onClick={handleSearch} disabled={loading} data-cy="search-button">
            {loading ? 'Buscando...' : 'Buscar'}
          </button>
        </div>
      </header>
      
      <main>
        {error && <p className="error-message">{error}</p>}
        {message && <p className="info-message">{message}</p>} {/* Novo elemento para mensagens */}
        
        {tracks.length > 0 && (
          <ul className="track-list">
            {tracks.map(track => (
              <li key={track.id} className="track-item" data-cy="track-item">
                {track.album && track.album.images.length > 0 && (
                  <img 
                    src={track.album.images[0].url} 
                    alt={`Capa do álbum de ${track.album.name}`} 
                    className="album-cover"
                  />
                )}
                
                <div className="track-info">
                  <span className="track-name">{track.name}</span>
                  <span className="track-artists">
                    {track.artists.map(artist => artist.name).join(', ')}
                  </span>
                </div>
                
                <button onClick={() => handleAddToQueue(track.uri)} data-cy="add-to-queue-button">
                  Adicionar à Fila
                </button>
              </li>
            ))}
          </ul>
        )}
      </main>
    </div>
  );
}

export default App;