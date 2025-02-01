'use client';

import React, { useState, useEffect, Suspense } from 'react';
import { fetchStatistics, fetchPopularAdverts, fetchAdvertsReport, fetchReportUsers, fetchTourRequestReports, getCategoriesAsList } from '@/services/report-service';
import jsPDF from 'jspdf';
import 'jspdf-autotable';
import { BiSolidFilePdf } from 'react-icons/bi';
import styles from '@/styles/components/admin/admin-report/admin-report.module.scss';
import { getAdvertTypes } from '@/services/advert-type-service';
import Swal from 'sweetalert2';
import Loading from '../loading';
import { wait } from '@/utils/wait';

export default function AdminReportsPage() {
  const [loading, setLoading] = useState(true);
  const [amount, setAmount] = useState(10);
  const [role, setRole] = useState('');
  const [categories, setCategories] = useState([]);
  const [advertTypes, setAdvertTypes] = useState([]);
  const [advertsParams, setAdvertsParams] = useState({
    date1: '',
    date2: '',
    category: '',
    type: '',
    status: ''
  });
  const [tourParams, setTourParams] = useState({
    date1: '',
    date2: '',
    status: ''
  });

  const advertStatusOptions = [
    { id: 0, label: 'Pending', value: 'PENDING' },
    { id: 1, label: 'Activated', value: 'ACTIVATED' },
    { id: 2, label: 'Rejected', value: 'REJECTED' }
  ];

  const roleOptions = [
    { id: 0, label: 'Admin', value: 'ADMIN' },
    { id: 1, label: 'Manager', value: 'MANAGER' },
    { id: 2, label: 'Customer', value: 'CUSTOMER' }
  ];

  const tourRequestStatusOptions = [
    { id: 0, label: 'Pending', value: 'PENDING' },
    { id: 1, label: 'Approved', value: 'APPROVED' },
    { id: 2, label: 'Declined', value: 'DECLINED' },
    { id: 3, label: 'Canceled', value: 'CANCELED' }
  ];

  useEffect(() => {
    const fetchData = async () => {
      try {
        const advertTypesData = await getAdvertTypes();
        setAdvertTypes(advertTypesData.object || []);
      } catch (error) {
        console.error('Error fetching advert types:', error);
      }
    };
    fetchData();
  }, []);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const categoriesData = await getCategoriesAsList();
        await wait(2000);
        setCategories(categoriesData || []);
      } catch (error) {
        console.error('Error fetching categories:', error);
      }finally{
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const generatePDF = (title, data, fields) => {
    const doc = new jsPDF();
    doc.text(title, 10, 10);
    
    if (!Array.isArray(data) || data.length === 0) {
        console.error('Data is not an array or is empty:', data);
        doc.text('No data available to generate PDF.', 10, 20);
        doc.save(`${title.toLowerCase().replace(/ /g, '-')}.pdf`);
        return;
    }

    const filteredData = data.map(item => {
        const filteredItem = {};
        fields.forEach(field => {
            filteredItem[field] = item[field];
        });
        return filteredItem;
    });

    const headers = fields.map(field => {
        switch (field) {
            case 'createdAt':
                return 'Create Time';
            case 'updatedAt':
                return 'Update Time';
            case 'tourDate':
                return 'Tour Date';    
            default:
                return field;
        }
    });

    const rows = filteredData.map(item => Object.values(item));

    doc.autoTable({
        head: [headers],
        body: rows,
        startY: 20,
    });

    doc.save(`${title.toLowerCase().replace(/ /g, '-')}.pdf`);
  };

  const handleAdvertsPDF = async () => {
    try {
        const { date1, date2, category, type, status } = advertsParams;
        if (!date1 || !date2 || !category || !type || !status) {
            await Swal.fire({
                title: 'Warning',
                text: 'Please fill in all fields.',
                icon: 'warning',
                confirmButtonText: 'Okay'
            });
            return;
        }

        // Normalize case for comparison
        const statusEnum = advertStatusOptions.find(option => option.label.toLowerCase() === status.toLowerCase())?.value || '';

        if (!statusEnum) {
            await Swal.fire({
                title: 'Warning',
                text: 'Geçerli bir durum seçin.',
                icon: 'warning',
                confirmButtonText: 'Tamam'
            });
            return;
        }

        const params = { ...advertsParams, status: statusEnum };
        const advertsResponse = await fetchAdvertsReport(params);
        
        const adverts = advertsResponse.object;
        
        if (!Array.isArray(adverts) || adverts.length === 0) {
            await Swal.fire({
                title: 'Warning',
                text: 'No data available to generate a report based on the selected criteria.',
                icon: 'warning',
                confirmButtonText: 'Okay'
            });
            return; // Cancel PDF generation
        }
        
        const enrichedAdverts = adverts.map(advert => ({
            ...advert,
            status: advertStatusOptions.find(option => option.value === advert.advertStatus)?.label || advert.advertStatus,
            createdAt: new Date(advert.createdAt).toLocaleString(),
            updatedAt: advert.updatedAt ? new Date(advert.updatedAt).toLocaleString() : 'N/A',
        }));

        generatePDF('Adverts Report', enrichedAdverts, ['title', 'desc', 'price', 'status', 'createdAt', 'updatedAt']);
    } catch (error) {
        console.error('Error generating adverts PDF:', error);
    }
  };

  const handlePopularAdvertsPDF = async () => {
    try {
        const popularAdsResponse = await fetchPopularAdverts(amount);
        
        if (!Array.isArray(popularAdsResponse.object) || popularAdsResponse.object.length === 0) {
            await Swal.fire({
                title: 'Warning',
                text: 'No data available to generate a report based on the selected criteria.',
                icon: 'warning',
                confirmButtonText: 'Okay'
            });
            return; // Cancel PDF generation
        }

        const popularAds = popularAdsResponse.object;

        // Map advertStatus to its label and format createdAt
        const enrichedPopularAds = popularAds.map(advert => ({
            ...advert,
            id: advert.id,
            title: advert.title,
            desc: advert.desc,
            price: advert.price,
            createdAt: new Date(advert.createdAt).toLocaleString(), // Format the date
            updatedAt: advert.updatedAt ? new Date(advert.updatedAt).toLocaleString() : 'N/A', // Handle null
            category: advert.category,
            type: advert.type,
            status: advertStatusOptions.find(option => option.value === advert.advertStatus)?.label || advert.advertStatus
        }));

        generatePDF('Popular Adverts', enrichedPopularAds, ['id', 'title', 'desc', 'price', 'createdAt', 'updatedAt', 'category', 'type', 'status']);
    } catch (error) {
        console.error('Error generating popular adverts PDF:', error);
    }
  };

  const handleUsersPDF = async () => {
    try {
        if (!role) {
            await Swal.fire({
                title: 'Warning',
                text: 'Please select a role.',
                icon: 'warning',
                confirmButtonText: 'Okay'
            });
            return;
        }

        const usersResponse = await fetchReportUsers(role);
        
        const users = usersResponse.object;
        
        if (!Array.isArray(users) || users.length === 0) {
            await Swal.fire({
                title: 'Warning',
                text: 'No data available to generate a report based on the selected criteria.',
                icon: 'warning',
                confirmButtonText: 'Okay'
            });
            return; // Cancel PDF generation
        }

        generatePDF('Users Report', users, ['firstName', 'lastName', 'email']);
    } catch (error) {
        console.error('Error generating users PDF:', error);
    }
  };

  const handleTourRequestsPDF = async () => {
    try {

      const { date1, date2, status } = tourParams;
        if (!date1 || !date2 || !status) {
            await Swal.fire({
                title: 'Warning',
                text: 'Please fill in all fields.',
                icon: 'warning',
                confirmButtonText: 'Okay'
            });
            return;
        }

        const tourRequestsResponse = await fetchTourRequestReports(tourParams);
        const tourRequests = tourRequestsResponse.object;

        if (!Array.isArray(tourRequests) || tourRequests.length === 0) {
            await Swal.fire({
                title: 'Warning',
                text: 'No data available to generate a report based on the selected criteria.',
                icon: 'warning',
                confirmButtonText: 'Okay'
            });
            return; // Cancel PDF generation
        }

        // Map the tour request data to include only the necessary fields
        const enrichedTourRequests = tourRequests.map(request => ({
            id: request.id,
            tourDate: new Date(request.tourDate).toLocaleString(), // Format the date
            status: request.tourRequestStatus,
            createdAt: new Date(request.createdAt).toLocaleString(), // Format the date
            updatedAt: request.updatedAt ? new Date(request.updatedAt).toLocaleString() : 'N/A', // Handle null
            advertId: request.advertId,
            ownerUserId: request.ownerUserId,
            guestUserId: request.guestUserId
        }));

        generatePDF('Tour Request Reports', enrichedTourRequests, ['id', 'tourDate', 'status', 'createdAt', 'updatedAt', 'advertId', 'ownerUserId', 'guestUserId']);
    } catch (error) {
        console.error('Error generating tour requests PDF:', error);
    }
  };

  return (
    loading ? 
    <Loading />
    :
    <div className={styles.container}>
      <div className={styles.section}>
        <h2>Adverts</h2>
        <div className={styles.inputGroup}>
          <input type="date" placeholder="Beginning date" onChange={(e) => setAdvertsParams({ ...advertsParams, date1: e.target.value })} />
          <input type="date" placeholder="Ending date" onChange={(e) => setAdvertsParams({ ...advertsParams, date2: e.target.value })} />
          <select onChange={(e) => setAdvertsParams({ ...advertsParams, category: e.target.value })}>
            <option value="">Select Category</option>
            {categories.map(category => (
              <option key={category.id} value={category.id}>{category.title}</option>
            ))}
          </select>
          <select onChange={(e) => setAdvertsParams({ ...advertsParams, type: e.target.value })}>
            <option value="">Select Type</option>
            {advertTypes.map(type => (
              <option key={type.id} value={type.id}>{type.title}</option>
            ))}
          </select>
          <select onChange={(e) => setAdvertsParams({ ...advertsParams, status: e.target.value })}>
            <option value="">Select Status</option>
            {advertStatusOptions.map(status => (
              <option key={status.id} value={status.value}>{status.label}</option>
            ))}
          </select>
          <div className={styles.iconContainer} onClick={handleAdvertsPDF}>
            <BiSolidFilePdf className={styles.icon} />
          </div>
        </div>
      </div>

      <div className={styles.section}>
        <h2>Most Popular Properties</h2>
        <div className={styles.inputGroup}>
          <input type="number" placeholder="Amount" value={amount} onChange={(e) => setAmount(e.target.value)} />
          <div className={styles.iconContainer} onClick={handlePopularAdvertsPDF}>
            <BiSolidFilePdf className={styles.icon} />
          </div>
        </div>
      </div>

      <div className={styles.section}>
        <h2>Users</h2>
        <div className={styles.inputGroup}>
          <select onChange={(e) => setRole(e.target.value)}>
            <option value="">Select Role</option>
            {roleOptions.map(role => (
              <option key={role.id} value={role.value}>{role.label}</option>
            ))}
          </select>
          <div className={styles.iconContainer} onClick={handleUsersPDF}>
            <BiSolidFilePdf className={styles.icon} />
          </div>
        </div>
      </div>

      <div className={styles.section}>
        <h2>Tour Requests</h2>
        <div className={styles.inputGroup}>
          <input type="date" placeholder="Beginning date" onChange={(e) => setTourParams({ ...tourParams, date1: e.target.value })} />
          <input type="date" placeholder="Ending date" onChange={(e) => setTourParams({ ...tourParams, date2: e.target.value })} />
          <select onChange={(e) => setTourParams({ ...tourParams, status: e.target.value })}>
            <option value="">Select Status</option>
            {tourRequestStatusOptions.map(status => (
              <option key={status.id} value={status.value}>{status.label}</option>
            ))}
          </select>
          <div className={styles.iconContainer} onClick={handleTourRequestsPDF}>
            <BiSolidFilePdf className={styles.icon} />
          </div>
        </div>
      </div>
    </div>
  );
}
