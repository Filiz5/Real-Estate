'use server';

import { config } from '@/helpers/config';
import { auth } from '@/auth';
import { getAuthHeader } from '@/helpers/auth-helper';

const API_URL = config.api.baseUrl;


export const resetDatabase = async () => {
  try {
    const response = await fetch(`${API_URL}/settings/db-reset`, {
      method: 'POST',
      headers: await getAuthHeader(),
    });

    if (!response.ok) {
      throw new Error('Failed to reset database.');
    }

    return { ok:true, message: 'Database reset successfully.' };
  } catch (error) {
    console.error('Error resetting database:', error);
    throw error;
  }
};

export const importData = async (countries) => {
  try {
    const response = await fetch(`${API_URL}/location/importData`, {
      method: 'POST',
      headers: await getAuthHeader(),
      body: JSON.stringify(countries),
    });

    if (!response.ok) {
        throw new Error("Failed to import data");
      }
  
      const data = await response.json();
      return data;
  } catch (error) {
    console.error('Error importing data:', error);
    throw error;
  }
};

export const deleteAllCountriesCitiesDistricts = async () => {
  try {
    const response = await fetch(`${API_URL}/location/deleteAllCountries`, {
      method: 'DELETE',
      headers: await getAuthHeader(),
    });

    if (!response.ok) {
      throw new Error('Failed to delete all countries.');
    }

    return await response.json();
  } catch (error) {
    console.error('Error deleting all countries:', error);
    throw error;
  }
}; 