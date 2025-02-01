"use client";

import React, { useEffect, useState } from 'react'
import { IoCheckmarkCircleOutline, IoTrashBinOutline } from 'react-icons/io5';
import styles from '@/styles/components/dashboards/update-advert/advert-fields/tour-requests-for-owner.module.scss';
import { ImCross } from 'react-icons/im';
import { MdOutlineCancel } from 'react-icons/md';
import { approveTourRequest, rejectTourRequest } from '@/services/tour-request-service';
import { swAlert, swConfirm } from '@/helpers/swal';
import { useRouter } from 'next/navigation';

export default function TourRequests({data}) {
    const [tourRequests, setTourRequests] = useState([]);
    const router = useRouter();

    console.log('tour requests', data);

    useEffect(() => {
        setTourRequests(data ?(data.filter(request => request.tourRequestStatus !== 'CANCELLED')) : []);
    }, [data]);

    const timeFix = (time) => {
        const [hour, minute] = time.split(':');
        return `${hour}:${minute}`;
    }


    const handleApprove = async (id) => {
        try{
          const answer = await swConfirm('Are you sure you want to approve this tour request?');
          if(!answer.isConfirmed) return;
          
          const res = await approveTourRequest(id);
          swAlert(res.message, 'success');
          router.refresh();

        }catch(error){
          swAlert(error.message, 'error');
        }
    }

    const handleReject = async (id) => {
        try {
          const answer = await swConfirm('Are you sure you want to decline this tour request?');
          if (!answer.isConfirmed) return;
          
          const res = await rejectTourRequest(id);
          swAlert(res.message, 'success');
          router.refresh();
        } catch (error) {
          swAlert(error.message, 'error');
        }
    }

  return (
    <div>
      <div className={styles.container}>
        <div className={styles.head}>
          <p className={styles.head_line}>Tour Requests</p>
          <p className={styles.sub_head_line}></p>
        </div>

        <div className={styles.table_container}>
          <table>
            <thead className={styles.table_head}>
              <tr>
                <th>Guest</th>
                <th>Status</th>
                <th>Created At</th>
                <th>Tour Date</th>
                <th>Tour Time</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody className={styles.table_body}>
              {Array.isArray(tourRequests) && tourRequests.length > 0 ? (
                tourRequests.map((request, index) => (
                  <tr key={index}>
                    <td>{`${request.guestFirstName} ${request.guestLastName}`}</td>
                    <td>
                      <span className={styles.status}>
                        {request.tourRequestStatus}
                      </span>
                    </td>
                    <td>
                      {new Date(request.createdAt).toLocaleDateString()}
                      <br />
                      <hr />
                      {timeFix(
                        new Date(request.createdAt).toTimeString()
                      )}
                    </td>
                    <td>{new Date(request.tourDate).toLocaleDateString()}</td>
                    <td>{timeFix(request.tourTime)}</td>
                    <td className={styles.action}>
                      <button
                        className={styles.reject_button}
                        onClick={() => handleReject(request.id)}
                        disabled={
                          request.tourRequestStatus === 'DECLINED' ||
                          request.tourRequestStatus === 'CANCELLED'
                        }
                      >
                        <MdOutlineCancel size={20} className={styles.reject}/>
                      </button>
                      <button
                        className={styles.approve_button}
                        onClick={() => handleApprove(request.id)}
                        disabled={
                          request.tourRequestStatus === 'APPROVED' ||
                          request.tourRequestStatus === 'CANCELLED'
                        }
                      >
                        <IoCheckmarkCircleOutline size={20} className={styles.approve}/>
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5">No tour requests</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
