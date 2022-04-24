/**
 * Convert object class constructs into strings
 * @param   {Object} classes - class list as object
 * @returns {string} return only the classes having a truthy value
 */
export default function classNames(classes) {
  return Object.entries(classes).reduce((acc, item) => {
    const [key, value] = item

    if (value) return [...acc, key]

    return acc
  }, []).join(' ')
}
