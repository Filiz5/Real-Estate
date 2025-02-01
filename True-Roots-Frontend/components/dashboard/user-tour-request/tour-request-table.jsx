'use client';
import styles from '@/styles/components/dashboards/user-tour-request/userTourRequestTable.module.scss';
import { cancelTourRequest } from '@/actions/cancel-tourRequest-actions';
import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Image from 'next/image';
import TourRequestSearchBox from './tour-request-search-box';
import { FaEdit } from 'react-icons/fa';
import { FcCancel, FcEmptyTrash } from 'react-icons/fc';
import Swal from 'sweetalert2';
import { swAlert, swConfirm } from '@/helpers/swal';
import { Pagination } from '@/components/common/Pagination';
import Spacer from '@/components/common/Spacer';
import {formatDateTime} from '@/helpers/functions/formatDateTime';
import { Suspense } from 'react';
import Loader from '@/components/common/Loader';
import { IoTrashBinOutline } from 'react-icons/io5';
import { CiEdit } from 'react-icons/ci';
import { MdOutlineCancel } from 'react-icons/md';
import { wait } from '@/utils/wait';
import { deleteTourRequest } from '@/services/tour-request-service';

export default function TourRequestTable({ data,  size,pageSize, page, sort, type}) {
  const router = useRouter();
  const { content } = data;
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredData, setFilteredData] = useState(content);
  const [currentURL, setCurrentURL] = useState('');
  const handleSearchTerm = (query) => {
    setSearchTerm(query);
  };

  console.log('filteredData', filteredData);

  useEffect(() => {
    const url = new URL(window.location.href);
    if (searchTerm) url.searchParams.set('q', searchTerm);
    else url.searchParams.delete('q');
    setFilteredData(content);
    router.push(url.toString());
  }, [searchTerm, data, router, content]);

  const timeFix = (time) => {
    const [hour, minute] = time.split(':');
    return `${hour}:${minute}`;
  };

  const handleEdit = (id) => {
    if (!id) {
      console.error('Invalid ID');
      return;
    }
    router.push(`/dashboard/tour-request/edit/${id}`);
  };

  const handleDelete = async (id) => {
    try {
      const answer = await swConfirm('Are you sure you want to delete this tour request?');
      if(!answer.isConfirmed) return;
      const res = await deleteTourRequest(id);
      swAlert(res.message, 'success');
      router.refresh();
    } catch (error) {
      swAlert(error.message, 'error');
    }
  };

  

  const handleCancel = async (id) => {
    try {
      const result = await Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, cancel it!'
      });

      if (result.isConfirmed) {
        const res = await cancelTourRequest(id);
        if(!res.success){
          swAlert(res.message, 'error', '', 4000);
        }else{
        Swal.fire({
          title: 'Canceled!',
          text: 'Tour request has been canceled.',
          icon: 'success'
        }).then(() => {
          window.location.reload();
        });
      }
    }
    } catch (error) {
      swAlert(error.message, 'error', '', 4000);
    }
  };

  useEffect(() => {

    if (typeof window !== 'undefined') {
      setCurrentURL(window.location.href); // Full URL
    }
  }, []);

  return (
    <div>
      <TourRequestSearchBox
        handleSearchTerm={handleSearchTerm}
        data={content}
      />
      <Suspense fallback={<Loader />}>
        <div className={styles.tableContainer}>
          {/* Table Layout for Larger Screens */}
          <table className={styles.table}>
            <thead>
              <tr>
                <th>Property</th>
                <th>Owner</th>
                <th>Status</th>
                <th>Tour Request Date</th>
                <th>Tour Request Time</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {filteredData.map((tourRequest, index) => (
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
                        <div>
                          {tourRequest?.advert?.district?.name},{' '}
                          {tourRequest?.advert?.city?.name},{' '}
                          {tourRequest?.advert?.country?.name}
                        </div>
                        <div>${tourRequest?.advert?.price}</div>
                      </div>
                    </div>
                  </td>
                  <td>
                    {tourRequest.ownerFirstName} {tourRequest.ownerLastName}
                  </td>
                  <td>{tourRequest.tourRequestStatus}</td>
                  <td>{tourRequest.tourDate}</td>
                  <td>{timeFix(tourRequest.tourTime)}</td>
                  <td>
                    <div className={styles.actions}>
                      <button
                        aria-label="Delete"
                        className={styles.actionButton}
                        onClick={() => handleCancel(tourRequest.id)}
                      >
                        <MdOutlineCancel
                          size={25}
                          className={styles.cancelIcone}
                        />
                      </button>
                      <button
                        aria-label="Edit"
                        className={styles.actionButton}
                        onClick={() => handleEdit(tourRequest.id)}
                      >
                        <CiEdit size={25} className={styles.edit} />
                      </button>
                      <button
                        aria-label="Edit"
                        className={styles.actionButton}
                        onClick={() => handleDelete(tourRequest.id)}
                      >
                        <IoTrashBinOutline
                          size={25}
                          className={styles.cancelIcone}
                        />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {/* Card Layout for Smaller Screens */}
          {content.map((tourRequest) => (
            <div className={styles.card} key={tourRequest.id}>
              <div className={styles.cardActions}>
                <button
                  className={styles.actionButton}
                  onClick={() => handleEdit(tourRequest.id)}
                >
                  <CiEdit color="purple" />
                </button>
                <button
                  className={styles.actionButton}
                  onClick={() => handleCancel(tourRequest.id)}
                >
                  <IoTrashBinOutline
                    className={styles.cancelIcone}
                    color="red"
                  />
                </button>
              </div>
              <div className={styles.cardImage}>
                <Image
                  src={
                    tourRequest.advert.images.find(
                      (image) => image.featured === true
                    )?.data || '/default-image.jpg'
                  }
                  alt="Main Property"
                  width={150}
                  height={100}
                />
              </div>
              <div className={styles.cardHeader}>
                {tourRequest.advert?.title || 'No Title'}
              </div>
              <div className={styles.cardBody}>
                <div className={styles.cardRow}>
                  <span className={styles.cardLabel}>Owner:</span>
                  <span>
                    {tourRequest.ownerFirstName} {tourRequest.ownerLastName}
                  </span>
                </div>
                <div className={styles.cardRow}>
                  <span className={styles.cardLabel}>Status:</span>
                  <span>{tourRequest.tourRequestStatus}</span>
                </div>
                <div className={styles.cardRow}>
                  <span className={styles.cardLabel}>Tour Date:</span>
                  <span>{tourRequest.tourDate}</span>
                </div>
                <div className={styles.cardRow}>
                  <span className={styles.cardLabel}>Tour Time:</span>
                  <span>{tourRequest.tourTime}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </Suspense>
      <Spacer height={40} />

      {data?.totalPages > 0 && (
        <Pagination
          baseUrl={currentURL}
          currentPage={page}
          size={size}
          totalPages={data?.totalPages || 1}
        />
      )}
    </div>
  );
}
