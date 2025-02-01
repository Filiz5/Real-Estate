"use client";

import React, { use, useEffect, useState } from 'react';
import Swal from 'sweetalert2';
import { resetDatabase, importData, deleteAllCountriesCitiesDistricts } from '@/actions/settings-actions';
import '@/styles/components/admin/admin-settings/admin-settings-module.scss';
import { wait } from '@/utils/wait';
import Loading from '../loading';
import { set } from 'react-hook-form';
import Loader from '@/components/common/Loader';

export default function AdminSettingsPage() {
  const [countries, setCountries] = useState([]);
  const [upLoading, setUpLoading] = useState(false);
  const [deletingCountries, setDeletingCountries] = useState(false);
  const [resettingDatabase, setResettingDatabase] = useState(false);

  const handleFileUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        try {
          const json = JSON.parse(e.target.result);
          setCountries(json);
          Swal.fire('Success', 'File uploaded successfully. Now you can import yourdata.', 'success');
        } catch (error) {
          Swal.fire('Error', 'Invalid JSON file.', 'error');
        }
      };
      reader.readAsText(file);
    }
  };

  const handleDbReset = async () => {
    const result = await Swal.fire({
      title: 'Are you sure?',
      text: "You are about to delete all records except those whose built-in fields are true. This action cannot be undone.",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, reset it!'
    });

    if (result.isConfirmed) {
      try {
        setResettingDatabase(true);
        const response = await resetDatabase();

        if (response.ok) {
        setResettingDatabase(false);
        Swal.fire('Success', 'Database reset successfully.', 'success');
      }else{
        setResettingDatabase(false);
        Swal.fire('Error', response.message || 'An error occurred while resetting database.', 'error');
      }
      } catch (error) {
        console.error('Error resetting database:', error);
        setResettingDatabase(false);
        Swal.fire('Error', 'Failed to reset database.', 'error');
      }
    }
  };

  const handleImportData = async () => {
    if (countries.length === 0) {
      Swal.fire('Warning', 'Please upload a JSON file first.', 'warning');
      return;
    }
    try {
      setUpLoading(true);
      const data = await importData(countries);

      if (data && data.message === "Successfully imported") {
        setUpLoading(false);
        Swal.fire('Success', data.message, 'success');
      } else {
        setUpLoading(false);
        Swal.fire('Error', data.message || 'An error occurred while importing data.', 'error');
      }
    } catch (error) {
      console.error('Error importing data:', error);
      setUpLoading(false);
      Swal.fire('Error', 'Failed to import data.', 'error');
    }
  };

  const handleDeleteAllCountries = async () => {
    const result = await Swal.fire({
      title: 'Are you sure?',
      text: "This will delete all countries, cities, and districts. This action cannot be undone.",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!'
    });

    if (result.isConfirmed) {
      try {
        setDeletingCountries(true);
        const data = await deleteAllCountriesCitiesDistricts();
        if (data.message) {
          setDeletingCountries(false);
          Swal.fire('Error', data.message || 'An error occurred while deleting data.', 'error');
        } else {
          setDeletingCountries(false);
          Swal.fire('Success', "All countries, cities, and districts have been deleted successfully.", 'success');

        }
      } catch (error) {
        console.error('Error deleting all countries:', error);
        setDeletingCountries(false);
        Swal.fire('Error', 'Failed to delete all countries.', 'error');
      }
    }
  };
  
  return (
    <div className="admin-settings-container">
      {upLoading && <div
        className="loading-container"
        >
      <Loader />
      <span>Data is upLoading... Please do not close your app</span>
      </div>}
      {deletingCountries && <div
        className="loading-container"
        >
        <Loader />
        <span>Data is deleting... Please do not close your app</span>
        </div>}
      {resettingDatabase && <div
        className="loading-container"
        >
        <Loader />
        <span>Database is resetting... Please do not close your app</span>
        </div>}
      <div className="alert-box">
        <h2>Reset Database</h2>
        <p>You are about to delete all records except those whose built-in fields are true. Are you sure to reset database?</p>
        <button className="reset-button" onClick={handleDbReset}>Reset Database</button>
      </div>
      <div className="alert-box">
        <h2>Import Countries, Cities and Districts</h2>
        <input type="file" accept=".json" onChange={handleFileUpload} />
        <button className="reset-button" onClick={handleImportData}>Import Data</button>
      </div>
      <div className="alert-box">
        <h2>Delete All Countries</h2>
        <p>This will delete all countries, cities, and districts. Are you sure?</p>
        <button className="reset-button" onClick={handleDeleteAllCountries}>Delete All Countries</button>
      </div>
    </div>
    
  );
}
