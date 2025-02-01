/**
 * Function to calculate and format the time elapsed since a given timestamp.
 * @param {string} createdAt - ISO 8601 formatted date string (e.g., "2024-12-04T13:15:34.232177").
 * @returns {string} - Human-readable time elapsed (e.g., "2 hours ago", "1 month ago").
 */
export function getTimeElapsed(createdAt) {
 
  const createdDate = new Date(createdAt);

  const currentDate = new Date();

  const differenceInTime = currentDate - createdDate;

  const oneHour = 1000 * 60 * 60;
  const oneDay = oneHour * 24;
  const oneWeek = oneDay * 7;
  const oneMonth = oneDay * 30; 
  const oneYear = oneDay * 365;

  let output;

  if (differenceInTime < oneDay) {
    const hours = Math.floor(differenceInTime / oneHour);
    output = `${hours} hour${hours !== 1 ? 's' : ''} ago`;
  } else if (differenceInTime < oneWeek) {
    const days = Math.floor(differenceInTime / oneDay);
    output = `${days} day${days !== 1 ? 's' : ''} ago`;
  } else if (differenceInTime < oneMonth) {
    const weeks = Math.floor(differenceInTime / oneWeek);
    output = `${weeks} week${weeks !== 1 ? 's' : ''} ago`;
  } else if (differenceInTime < oneYear) {
    const months = Math.floor(differenceInTime / oneMonth);
    output = `${months} month${months !== 1 ? 's' : ''} ago`;
  } else {
    const years = Math.floor(differenceInTime / oneYear);
    output = `${years} year${years !== 1 ? 's' : ''} ago`;
  }

  return output;
}


