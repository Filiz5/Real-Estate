import { config } from '@/helpers/config';
import { getAuthHeader } from '@/helpers/auth-helper';

const API_URL = config.api.baseUrl;

export const login = (payload) => {
  return fetch(`${API_URL}/login`, {
    method: 'POST',
    body: JSON.stringify(payload),
    headers: {
      'Content-Type': 'application/json'
    }
  });
};

export const loginWithGoogle = (payload) => {
  return fetch(`${API_URL}/google-login`, {
    method: 'POST',
    body: JSON.stringify(payload),
    headers: {
      'Content-Type': 'application/json'
    }
  });
}

export const register = async (payload) => {
  const response = await fetch(`${API_URL}/register`, {
    method: 'POST',
    body: JSON.stringify(payload),
    headers: {
      'Content-Type': 'application/json'
    }
  });
  return response;
};

export const forgotPassword = async (payload) => {
  const response = await fetch(`${API_URL}/forgot-password`, {
    method: 'POST',
    body: JSON.stringify(payload),
    headers: {
      'Content-Type': 'application/json'
    }
  });
  const data = await response.json();
  if (!response.ok) {
    return { ok: false, message:  data.message };
  }
  return { ok: true, message: data.message };
}

export const resetPassword = async (payload) => {
  const response = await fetch(`${API_URL}/reset-password`, {
    method: 'POST',
    body: JSON.stringify(payload),
    headers: {
      'Content-Type': 'application/json'
    }
  });
  const data = await response.json();
  if (!response.ok) {
    return { ok: false, message:  data.message };
  }
  return { ok: true, message: "Your password is changed successfully. You can login now!" };
}
