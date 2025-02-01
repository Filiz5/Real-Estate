'use client';
import { deleteTourRequest } from '@/services/admin-tour-request';
import styles from '@/styles/components/admin/admin-tour-request/admin-tour-request-table.module.scss';
import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import TourRequestSearchBox from './tour-request-search-box';
import { FaTrash, FaList } from 'react-icons/fa';
import Swal from 'sweetalert2';
import { swAlert } from '@/helpers/swal';
import Image from 'next/image';
import NoDateFound from '@/components/common/NoDateFound';

export default function TourRequestTable({ data }) {
  const router = useRouter();
  const { content } = data;
  const [searchTerm, setSearchTerm] = useState('');
  const [tourRequest, setTourRequest] = useState('');
  const [filteredData, setFilteredData] = useState(content);

  const handleSearchTerm = (query) => {
    setSearchTerm(query);
  };

  useEffect(() => {
    const url = new URL(window.location.href);
    if (searchTerm) url.searchParams.set('q', searchTerm);
    else url.searchParams.delete('q');
    setFilteredData(content);
    router.push(url.toString());
  }, [searchTerm, data, router, content]);

  const handleDetailsPage = (id) => {
    if (!id) {
      console.error('Invalid ID');
      return;
    }
    router.push(`/admin-dashboard/admin-tour-requests/${id}`);
  };

  const handleDelete = async (id) => {
    try {
      const result = await Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
      });

      if (!result.isConfirmed) {
        return;
      }
      const res = await deleteTourRequest(id);
      if (res.success) {
        swAlert(res.message, 'success');
        router.refresh();
      } else {
        swAlert(res.message, 'error');
      }
    } catch (error) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: error.message
      });
    }
  };

  function capitalize(name) {
    if (!name) return '';
    return name.charAt(0).toUpperCase() + name.slice(1).toLowerCase();
  }

  return (
    <div>
      <TourRequestSearchBox
        handleSearchTerm={handleSearchTerm}
        data={tourRequest}
      />
      {filteredData.length === 0 ? 
      <NoDateFound text={'No Tour Requests found'} />
          :
      <div className={styles.tableContainer}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>Property</th>
              <th>Owner</th>
              <th>Guest</th>
              <th>Status</th>
              <th>Tour Date</th>
              <th>Tour Time</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody className={styles.tableBody}>
            {filteredData.map((tourRequest) => (
              <tr key={tourRequest.id}>
                <td>
                  <div className={styles.advert}>
                    <Image
                      src={
                        tourRequest.advert.images.find(
                          (image) => image.featured === true
                        )?.data || '/assets/images/image-not-found.jpg'
                      }
                      alt="Main Property"
                      className={styles.advert_image}
                      width={150}
                      height={100}
                      quality={100}
                      priority
                    />
                    <div className={styles.advertBody}>
                      <div className={styles.advertTitle}>
                        {tourRequest?.advert?.title}
                      </div>
                      <div className={styles.advertLocation}>
                        {tourRequest?.advert?.district?.name},{' '}
                        {tourRequest?.advert?.city?.name},{' '}
                        {tourRequest?.advert?.country?.name}
                      </div>
                      <div>${' '}{tourRequest?.advert?.price.toFixed(2)}</div>
                    </div>
                  </div>
                </td>
                <td>
                  {capitalize(tourRequest.ownerFirstName)}{' '}
                  {capitalize(tourRequest.ownerLastName)}
                </td>
                <td>
                  {capitalize(tourRequest.guestFirstName)}{' '}
                  {capitalize(tourRequest.guestLastName)}
                </td>
                <td>
                  <span className={styles.status}>
                    {tourRequest.tourRequestStatus}
                  </span>
                </td>
                <td>{new Date(tourRequest.tourDate).toLocaleDateString()}</td>
                <td>
                  {new Date(tourRequest.tourDate).toLocaleTimeString([], {
                    hour: '2-digit',
                    minute: '2-digit'
                  })}
                </td>
                <td>
                  <button
                    aria-label="Delete"
                    className={styles.actionButton}
                    onClick={() => handleDelete(tourRequest.id)}
                  >
                    <FaTrash color="red" />
                  </button>
                  <button
                    aria-label="Details"
                    className={styles.actionButton}
                    onClick={() => handleDetailsPage(tourRequest.id)}
                  >
                    <FaList color="blue" />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {/* Card Layout for Smaller Screens */}
        {filteredData.map((tourRequest) => (
          <div className={styles.card} key={tourRequest.id}>
            <div className={styles.cardActions}>
              <button
                className={styles.actionButton}
                onClick={() => handleDetailsPage(tourRequest.id)}
              >
                <FaList color="blue" />
              </button>
              <button
                className={styles.actionButton}
                onClick={() => handleDelete(tourRequest.id)}
              >
                <FaTrash color="red" />
              </button>
            </div>
            <div className={styles.cardImage}>
              <Image
                src={
                  tourRequest.advert.images.find(
                    (image) => image.featured === true
                  )?.data || '/assets/images/image-not-found.jpg'
                }
                alt="Main Property"
                width={150}
                height={100}
              />
            </div>
            <div className={styles.cardHeader}>
              {tourRequest.advert?.title || 'No Title'}
            </div>
            <div className={styles.cardLocation}>
              {tourRequest?.advert?.district?.name},{' '}
              {tourRequest?.advert?.city?.name},{' '}
              {tourRequest?.advert?.country?.name}
            </div>
            <div className={styles.cardLocation}>
            ${' '}{tourRequest?.advert?.price.toFixed(2)}
            </div>
            <div className={styles.cardBody}>
              <div className={styles.cardRow}>
                <span className={styles.cardLabel}>Owner:</span>
                <span className={styles.cardLabel}>
                  {capitalize(tourRequest.ownerFirstName)}{' '}
                  {capitalize(tourRequest.ownerLastName)}
                </span>
              </div>
              <div className={styles.cardRow}>
                <span className={styles.cardLabel}>Guest:</span>
                <span className={styles.cardLabel}>
                  {capitalize(tourRequest.guestFirstName)}{' '}
                  {capitalize(tourRequest.guestLastName)}
                </span>
              </div>
              <div className={styles.cardRow}>
                <span className={styles.cardLabel}>Status:</span>
                <span className={styles.cardStatus}>
                  {tourRequest.tourRequestStatus}
                </span>
              </div>
              <div className={styles.cardRow}>
                <span className={styles.cardLabel}>Tour Date:</span>
                <span className={styles.cardLabel}>
                  {new Date(tourRequest.tourDate).toLocaleDateString()}
                </span>
              </div>
              <div className={styles.cardRow}>
                <span className={styles.cardLabel}>Tour Time:</span>
                <span className={styles.cardLabel}>
                  {new Date(tourRequest.tourDate).toLocaleTimeString([], {
                    hour: '2-digit',
                    minute: '2-digit'
                  })}
                </span>
              </div>
            </div>
          </div>
        ))}
      </div>
      }
    </div>
  );
}
