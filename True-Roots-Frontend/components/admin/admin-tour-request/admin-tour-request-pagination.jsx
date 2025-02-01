'use client';

import React from 'react';
import { useRouter } from 'next/navigation';
import styles from '@/styles/components/admin/admin-tour-request/admin-tour-request-pagination.module.scss';
import { Pagination } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

const Paginations = ({ currentPage, totalPages, searchParams }) => {
  const router = useRouter();

  const handlePageChange = (newPage) => {
    const updatedParams = new URLSearchParams(searchParams);
    updatedParams.set('page', newPage);
    router.push(
      `/admin-dashboard/admin-tour-requests?${updatedParams.toString()}`
    );
  };

  return (
    <div className={styles.pagination}>
      <Pagination className={styles.customPagination}>
        <Pagination.First
          disabled={currentPage <= 1}
          onClick={() => handlePageChange(currentPage - 1)}
        />

        <Pagination.Prev
          disabled={currentPage <= 1}
          onClick={() => handlePageChange(currentPage - 1)}
        />

        <Pagination.Item style={{ pointerEvents: 'none' }}>
          {currentPage} / {totalPages}
        </Pagination.Item>

        <Pagination.Next
          disabled={currentPage >= totalPages}
          onClick={() => handlePageChange(currentPage + 1)}
        />

        <Pagination.Last
          disabled={currentPage >= totalPages}
          onClick={() => handlePageChange(currentPage + 1)}
        />
      </Pagination>
    </div>
  );
};

export default Paginations;
