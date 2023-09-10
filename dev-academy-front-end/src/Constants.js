export const severityValues = [
    { value: 'MILD', label: 'MILD' },
    { value: 'MODERATE', label: 'MODERATE' },
    { value: 'CRITICAL', label: 'CRITICAL' }
];

export const statusValues = [
    { value: 'ACTIVE', label: 'ACTIVE' },
    { value: 'INACTIVE', label: 'INACTIVE' }
];

export const navBarStyle = {
    outline: 0,
    border: 0,
    margin: 0,
    borderRadius: 0,
    padding: '5px 10px',
    cursor: 'pointer',
    userSelect: 'none',
    verticalAlign: 'middle',
    color: 'inherit',
    fontFamily: '"Roboto","Helvetica","Arial",sans-serif',
    fontWeight: 400,
    fontSize: '1rem',
    lineHeight: 1.5,
    letterSpacing: '0.00938em',
    display: 'flex',
    WebkitBoxPack: 'start',
    justifyContent: 'flex-start',
    WebkitBoxAlign: 'center',
    alignItems: 'center',
    position: 'relative'
};

export const customStyles = {
    option: provided => ({
        ...provided,
        color: 'black'
    }),
    control: provided => ({
        ...provided,
        color: 'black'
    }),
    singleValue: provided => ({
        ...provided,
        color: 'black'
    })
};