import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import { describe, test, expect, beforeEach, vi } from 'vitest';
import App from '../App';

// Mock fetch für API-Calls
global.fetch = vi.fn();

describe('Slot Machine App Tests', () => {
  beforeEach(() => {
    fetch.mockClear();
  });

  // Test 1: App lädt ohne Crashes
  test('1. App renders without crashing', () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => []
    });
    
    render(<App />);
    expect(screen.getByText('CASINO ROYALE')).toBeInTheDocument();
  });

  // Test 2: Player Creation Form
  test('2. Player creation form validation works', () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => []
    });

    render(<App />);
    
    const nameInput = screen.getByPlaceholderText('Spielername eingeben...');
    const coinsInput = screen.getByPlaceholderText('Start-Coins (Standard: 1000)');
    
    expect(nameInput).toBeInTheDocument();
    expect(coinsInput).toHaveAttribute('max', '1000');
    expect(coinsInput).toHaveAttribute('min', '0');
  });

  // Test 3: Player Creation mit gültigen Daten
  test('3. Player can be created with valid data', async () => {
    fetch
      .mockResolvedValueOnce({
        ok: true,
        json: async () => []
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({ id: 1, name: 'Test Player', coins: 500 })
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => [{ id: 1, name: 'Test Player', coins: 500 }]
      });

    render(<App />);
    
    const nameInput = screen.getByPlaceholderText('Spielername eingeben...');
    const coinsInput = screen.getByPlaceholderText('Start-Coins (Standard: 1000)');
    const submitButton = screen.getByText('✨ Spieler erstellen');

    fireEvent.change(nameInput, { target: { value: 'Test Player' } });
    fireEvent.change(coinsInput, { target: { value: '500' } });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith('http://localhost:8080/player', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: 'Test Player', coins: 500 })
      });
    });
  });

  // Test 4: Player Selection funktioniert
  test('4. Player selection shows game screen', async () => {
    const mockPlayer = { id: 1, name: 'Test Player', coins: 1000 };
    
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [mockPlayer]
    });

    render(<App />);
    
    await waitFor(() => {
      expect(screen.getByText('Test Player')).toBeInTheDocument();
    });

    fireEvent.click(screen.getByText('Test Player'));
    
    await waitFor(() => {
      expect(screen.getByText('🎰 SLOT MACHINE')).toBeInTheDocument();
      expect(screen.getByText('🎮 Test Player')).toBeInTheDocument();
    });
  });

  // Test 5: Slot Machine Button Disabled bei wenig Coins
  test('5. Slot machine button disabled when insufficient coins', async () => {
    const mockPlayer = { id: 1, name: 'Poor Player', coins: 5 };
    
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [mockPlayer]
    });

    render(<App />);
    
    await waitFor(() => {
      fireEvent.click(screen.getByText('Poor Player'));
    });

    await waitFor(() => {
      const pullButton = screen.getByText('🕹️ PULL (10 Coins)');
      expect(pullButton).toBeDisabled();
    });
  });
});
