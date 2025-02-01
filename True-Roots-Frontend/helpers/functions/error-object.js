/**
 * @param {string} message
 * @param {object} errors
 * @returns {{status: string, message: string, errors: object }}
 */


export const errorObject = (message, errors) => {

    return{
        status: "error",
        message,
        errors
    }
}