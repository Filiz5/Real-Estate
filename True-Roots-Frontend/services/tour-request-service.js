"use server";

import { getAuthHeader } from '@/helpers/auth-helper';

import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl;

export const approveTourRequest = async (id) => {
    try {
        const response = await fetch(`${API_URL}/tour-requests/${id}/approve`, {
        method: 'PATCH',
        headers: await getAuthHeader(),
        });
    
        if (!response.ok) {
        throw new Error(`Failed to approve tour request`);
        }
        const data = await response.json();
    
        return {message: 'Tour request approved successfully'};
    } catch (error) {
        console.error('Error approving tour request:', error.message);
        throw new Error('Something went wrong while approving tour request. Please try again later');
    }
    };

export const rejectTourRequest = async (id) => {
    try {
        const response = await fetch(`${API_URL}/tour-requests/${id}/decline`, {
        method: 'PATCH',
        headers: await getAuthHeader(),
        });
    
        if (!response.ok) {
        throw new Error(`Failed to reject tour request`);
        }
        const data = await response.json();
    
        return {message: 'Tour request rejected'};
    } catch (error) {
        console.error('Error rejecting tour request:', error.message);
        throw new Error('Something went wrong while rejecting tour request. Please try again later');
    }
    }

    export const deleteTourRequest = async (id) => {
        try {
            const response = await fetch(`${API_URL}/tour-requests/${id}/delete`, {
            method: 'DELETE',
            headers: await getAuthHeader(),
            });
        
            if (!response.ok) {
            throw new Error(`Failed to delete tour request`);
            }
            const data = await response.json();
        
            return {message: 'Tour request deleted'};
        } catch (error) {
            console.error('Error deleting tour request:', error.message);
            throw new Error('Something went wrong while deleting tour request. Please try again later');
        }
        }