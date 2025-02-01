import styles from '@/styles/components/common/pagination.module.scss';
import Link from 'next/link';
import { IoChevronBackOutline, IoChevronForwardOutline } from 'react-icons/io5';

export const Pagination = ({ baseUrl, currentPage, size, totalPages }) => {
    const previousPage = currentPage === 1 ? currentPage : currentPage - 1;
    const nextPage = currentPage === totalPages ? currentPage : currentPage + 1;

    const renderDirectPageLinks = () => {
        return Array.from({ length: totalPages }, (_, index) => (
            <Link
                key={index}
                href={`${baseUrl}/?size=${size}&page=${index + 1}`}
                title={`Go to page ${index + 1}`}
                className={`${styles.pageItem} ${
                    currentPage === index + 1 ? styles.active : ''
                }`}
            >
                {index + 1}
            </Link>
        ));
    };

    const renderPaginationWithEllipsis = () => {
        let startPage, endPage;

        if (currentPage <= 3) {
            startPage = 2;
            endPage = 4;
        } else if (currentPage >= totalPages - 2) {
            startPage = totalPages - 3;
            endPage = totalPages - 1;
        } else {
            startPage = currentPage - 1;
            endPage = currentPage + 1;
        }

        return (
            <>
                <Link
                    href={`${baseUrl}?size=${size}&page=1`}
                    title="Go to first page"
                    key={'page-1'}
                    className={`${styles.pageItem} ${
                        currentPage === 1 ? styles.active : ''
                    }`}
                >
                    1
                </Link>

                <span className={styles.ellipsis}>...</span>

                {Array.from(
                    { length: endPage - startPage + 1 },
                    (_, index) => startPage + index
                ).map((page) => (
                    <Link
                        key={`page-${page}`}
                        href={`${baseUrl}?size=${size}&page=${page}`}
                        title={`Go to page ${page}`}
                        className={`${styles.pageItem} ${
                            currentPage === page ? styles.active : ''
                        }`}
                    >
                        {page}
                    </Link>
                ))}

                <span className={styles.ellipsis}>...</span>

                <Link
                    href={`${baseUrl}?size=${size}&page=${totalPages}`}
                    title={`Go to page ${totalPages}`}
                    key={`page-${totalPages}`}
                    className={`${styles.pageItem} ${
                        currentPage === totalPages ? styles.active : ''
                    }`}
                >
                    {totalPages}
                </Link>
            </>
        );
    };

    return (
        <div className={styles.pagination}>
            {/* FIRST PAGE */}
            <Link
                href={`${baseUrl}?size=${size}&page=1`}
                className={`${styles.prev} ${styles.first} ${
                    currentPage === 1 ? styles.disabled : ''
                }`}
                title="Go to first page"
            >
                <IoChevronBackOutline size={30} />
                <IoChevronBackOutline size={30} />
            </Link>
            {/* PREVIOUS PAGE */}
            <Link
                href={`${baseUrl}?size=${size}&page=${previousPage}`}
                className={`${styles.prev} ${
                    currentPage === 1 ? styles.disabled : ''
                }`}
                title="Go to previous page"
            >
                <IoChevronBackOutline size={30} />
            </Link>
            {/* CURRENT PAGE */}
            <div className={styles.pages}>
                {totalPages < 5
                    ? renderDirectPageLinks()
                    : renderPaginationWithEllipsis()}
            </div>
            {/* NEXT PAGE */}
            <Link
                href={`${baseUrl}?size=${size}&page=${nextPage}`}
                className={`${styles.next} ${
                    currentPage === totalPages ? styles.disabled : ''
                }`}
                title="Go to next page"
            >
                <IoChevronForwardOutline size={30} />
            </Link>
            {/* LAST PAGE */}
            <Link
                href={`${baseUrl}?size=${size}&page=${totalPages}`}
                className={`${styles.next} ${styles.last} ${
                    currentPage === totalPages ? styles.disabled : ''
                }`}
                title="Go to last page"
            >
                <IoChevronForwardOutline size={30} />
                <IoChevronForwardOutline size={30} />
            </Link>
        </div>
    );
};
