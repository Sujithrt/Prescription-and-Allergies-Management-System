import axios from "axios"
import MockAdapter from "axios-mock-adapter"
import {
    retrievePatients,
    retrieveMedicines,
    retrieveIngredients,
    retrievePatientDetailsById,
    addNewAllergy,
    getRecentPatientsForUser,
    getPrescriptionHistory,
    prescribeMedicine
} from './../api/PnamsService'

describe('PNAMS Services test', () => {
    let mock;

    beforeAll(() => {
        mock = new MockAdapter(axios);
    });

    afterEach(() => {
        mock.reset();
    });
    it('check if retrievePatients is working', () => {
        const patientList = [
            {
                "id": 1001,
                "firstName": "Leanne",
                "lastName": "Graham",
                "gender": "Male",
                "age": 21,
                "phoneNumber": 9123456780,
                "email": "leanne.graham@gmail.com",
                "city": "Bangalore",
                "state": "Karnataka",
                "medicalConditions": [
                    {
                        "id": 1,
                        "name": "Cephalalgia (headache)"
                    },
                    {
                        "id": 3,
                        "name": "Otitis externa (swimmer's ear)"
                    },
                    {
                        "id": 9,
                        "name": "Contusion (bruise)"
                    }
                ]
            }
        ]
        const searchText = "lea"
        mock.onGet().reply(200, patientList)
        retrievePatients(searchText).then(response => {
            expect(response.data).toEqual(patientList)
        }).catch(error => {
            console.log(error)
        })
    })
    it('check if retrieveMedicines is working', () => {
        const medicineList = [
            {
                "id": 1,
                "brandName": "Dolo",
                "cause": "Incompatible with patients' medical conditions"
            },
            {
                "id": 13,
                "brandName": "Vibramycin",
                "cause": ""
            },
            {
                "id": 17,
                "brandName": "Vibramycin",
                "cause": ""
            },
            {
                "id": 22,
                "brandName": "Betadine",
                "cause": ""
            },
            {
                "id": 25,
                "brandName": "Iodex",
                "cause": ""
            },
            {
                "id": 46,
                "brandName": "Desyrel",
                "cause": "Incompatible with patients' medical conditions"
            }
        ]
        const searchText = "d"
        mock.onGet().reply(200, medicineList)
        retrieveMedicines(searchText).then(response => {
            expect(response.data).toEqual(medicineList)
        }).catch(error => {
            console.log(error)
        })
    })
    it('check if retrieveIngredients is working', () => {
        const ingredientsList = [
            {
                "id": 1,
                "name": "Paracetamol"
            },
            {
                "id": 34,
                "name": "Estropipate"
            },
            {
                "id": 47,
                "name": "Lorazepam"
            }
        ]
        const searchText = "Pa"
        mock.onGet().reply(200, ingredientsList)
        retrieveIngredients(searchText).then(response => {
            expect(response.data).toEqual(ingredientsList)
        }).catch(error => {
            console.log(error)
        })
    })
    it('check if retrievePatientDetailsById is working', () => {
        const patient = {
            "id": 1001,
            "firstName": "Leanne",
            "lastName": "Graham",
            "gender": "Male",
            "age": 21,
            "phoneNumber": 9123456780,
            "email": "leanne.graham@gmail.com",
            "city": "Bangalore",
            "state": "Karnataka",
            "medicalConditions": [
                {
                    "id": 1,
                    "name": "Cephalalgia (headache)"
                },
                {
                    "id": 3,
                    "name": "Otitis externa (swimmer's ear)"
                },
                {
                    "id": 9,
                    "name": "Contusion (bruise)"
                }
            ],
            "allergies": [
                {
                    "id": 7,
                    "name": "Aspirin Allergy",
                    "reaction": "Tiredness",
                    "severity": "MODERATE",
                    "status": "ACTIVE",
                    "ingredient": {
                        "id": 4,
                        "name": "Aspirin"
                    },
                    "encounter": {
                        "id": 10,
                        "date": "2021-10-26",
                        "patient": {
                            "id": 1001,
                            "firstName": "Leanne",
                            "lastName": "Graham",
                            "gender": "Male",
                            "age": 21,
                            "phoneNumber": 9123456780,
                            "email": "leanne.graham@gmail.com",
                            "city": "Bangalore",
                            "state": "Karnataka",
                            "medicalConditions": [
                                {
                                    "id": 1,
                                    "name": "Cephalalgia (headache)"
                                },
                                {
                                    "id": 3,
                                    "name": "Otitis externa (swimmer's ear)"
                                },
                                {
                                    "id": 9,
                                    "name": "Contusion (bruise)"
                                }
                            ]
                        },
                        "user": {
                            "id": 1,
                            "username": "John",
                            "password": "$2a$10$uS1MVfXzQkqbY7XDruby/OySJp3yjLmo4QGDSiE30GoN0wGb7DCqS",
                            "email": "john.cage@gmail.com",
                            "enabled": true,
                            "role": "Physician",
                            "authorities": [
                                {
                                    "id": 1,
                                    "roleCode": "USER",
                                    "roleDescription": "User role",
                                    "authority": "USER"
                                },
                                {
                                    "id": 2,
                                    "roleCode": "ADMIN",
                                    "roleDescription": "Admin role",
                                    "authority": "ADMIN"
                                }
                            ],
                            "credentialsNonExpired": true,
                            "accountNonExpired": true,
                            "accountNonLocked": true
                        }
                    },
                    "patient": {
                        "id": 1001,
                        "firstName": "Leanne",
                        "lastName": "Graham",
                        "gender": "Male",
                        "age": 21,
                        "phoneNumber": 9123456780,
                        "email": "leanne.graham@gmail.com",
                        "city": "Bangalore",
                        "state": "Karnataka",
                        "medicalConditions": [
                            {
                                "id": 1,
                                "name": "Cephalalgia (headache)"
                            },
                            {
                                "id": 3,
                                "name": "Otitis externa (swimmer's ear)"
                            },
                            {
                                "id": 9,
                                "name": "Contusion (bruise)"
                            }
                        ]
                    }
                },
                {
                    "id": 8,
                    "name": "Paracetamol",
                    "reaction": "sdfd",
                    "severity": "MILD",
                    "status": "ACTIVE",
                    "ingredient": {
                        "id": 1,
                        "name": "Paracetamol"
                    },
                    "encounter": {
                        "id": 13,
                        "date": "2021-10-26",
                        "patient": {
                            "id": 1001,
                            "firstName": "Leanne",
                            "lastName": "Graham",
                            "gender": "Male",
                            "age": 21,
                            "phoneNumber": 9123456780,
                            "email": "leanne.graham@gmail.com",
                            "city": "Bangalore",
                            "state": "Karnataka",
                            "medicalConditions": [
                                {
                                    "id": 1,
                                    "name": "Cephalalgia (headache)"
                                },
                                {
                                    "id": 3,
                                    "name": "Otitis externa (swimmer's ear)"
                                },
                                {
                                    "id": 9,
                                    "name": "Contusion (bruise)"
                                }
                            ]
                        },
                        "user": {
                            "id": 1,
                            "username": "John",
                            "password": "$2a$10$uS1MVfXzQkqbY7XDruby/OySJp3yjLmo4QGDSiE30GoN0wGb7DCqS",
                            "email": "john.cage@gmail.com",
                            "enabled": true,
                            "role": "Physician",
                            "authorities": [
                                {
                                    "id": 1,
                                    "roleCode": "USER",
                                    "roleDescription": "User role",
                                    "authority": "USER"
                                },
                                {
                                    "id": 2,
                                    "roleCode": "ADMIN",
                                    "roleDescription": "Admin role",
                                    "authority": "ADMIN"
                                }
                            ],
                            "credentialsNonExpired": true,
                            "accountNonExpired": true,
                            "accountNonLocked": true
                        }
                    },
                    "patient": {
                        "id": 1001,
                        "firstName": "Leanne",
                        "lastName": "Graham",
                        "gender": "Male",
                        "age": 21,
                        "phoneNumber": 9123456780,
                        "email": "leanne.graham@gmail.com",
                        "city": "Bangalore",
                        "state": "Karnataka",
                        "medicalConditions": [
                            {
                                "id": 1,
                                "name": "Cephalalgia (headache)"
                            },
                            {
                                "id": 3,
                                "name": "Otitis externa (swimmer's ear)"
                            },
                            {
                                "id": 9,
                                "name": "Contusion (bruise)"
                            }
                        ]
                    }
                },
                {
                    "id": 13,
                    "name": "Ibuprofin Allergy",
                    "reaction": "Increased Perspiration",
                    "severity": "MODERATE",
                    "status": "ACTIVE",
                    "ingredient": {
                        "id": 3,
                        "name": "Ibuprofen"
                    },
                    "encounter": {
                        "id": 20,
                        "date": "2021-10-29",
                        "patient": {
                            "id": 1001,
                            "firstName": "Leanne",
                            "lastName": "Graham",
                            "gender": "Male",
                            "age": 21,
                            "phoneNumber": 9123456780,
                            "email": "leanne.graham@gmail.com",
                            "city": "Bangalore",
                            "state": "Karnataka",
                            "medicalConditions": [
                                {
                                    "id": 1,
                                    "name": "Cephalalgia (headache)"
                                },
                                {
                                    "id": 3,
                                    "name": "Otitis externa (swimmer's ear)"
                                },
                                {
                                    "id": 9,
                                    "name": "Contusion (bruise)"
                                }
                            ]
                        },
                        "user": {
                            "id": 1,
                            "username": "John",
                            "password": "$2a$10$uS1MVfXzQkqbY7XDruby/OySJp3yjLmo4QGDSiE30GoN0wGb7DCqS",
                            "email": "john.cage@gmail.com",
                            "enabled": true,
                            "role": "Physician",
                            "authorities": [
                                {
                                    "id": 1,
                                    "roleCode": "USER",
                                    "roleDescription": "User role",
                                    "authority": "USER"
                                },
                                {
                                    "id": 2,
                                    "roleCode": "ADMIN",
                                    "roleDescription": "Admin role",
                                    "authority": "ADMIN"
                                }
                            ],
                            "credentialsNonExpired": true,
                            "accountNonExpired": true,
                            "accountNonLocked": true
                        }
                    },
                    "patient": {
                        "id": 1001,
                        "firstName": "Leanne",
                        "lastName": "Graham",
                        "gender": "Male",
                        "age": 21,
                        "phoneNumber": 9123456780,
                        "email": "leanne.graham@gmail.com",
                        "city": "Bangalore",
                        "state": "Karnataka",
                        "medicalConditions": [
                            {
                                "id": 1,
                                "name": "Cephalalgia (headache)"
                            },
                            {
                                "id": 3,
                                "name": "Otitis externa (swimmer's ear)"
                            },
                            {
                                "id": 9,
                                "name": "Contusion (bruise)"
                            }
                        ]
                    }
                },
                {
                    "id": 14,
                    "name": "Paracetamol Allergy",
                    "reaction": "Increased Perspiration",
                    "severity": "MODERATE",
                    "status": "ACTIVE",
                    "ingredient": {
                        "id": 3,
                        "name": "Ibuprofen"
                    },
                    "encounter": {
                        "id": 1,
                        "date": "2021-10-26",
                        "patient": {
                            "id": 1003,
                            "firstName": "Clementine",
                            "lastName": "Bauch",
                            "gender": "Female",
                            "age": 56,
                            "phoneNumber": 9123456782,
                            "email": "clementine.bauch@gmail.com",
                            "city": "Mysore",
                            "state": "Karnataka",
                            "medicalConditions": [
                                {
                                    "id": 23,
                                    "name": "Gestation (pregnancy) "
                                }
                            ]
                        },
                        "user": {
                            "id": 1,
                            "username": "John",
                            "password": "$2a$10$uS1MVfXzQkqbY7XDruby/OySJp3yjLmo4QGDSiE30GoN0wGb7DCqS",
                            "email": "john.cage@gmail.com",
                            "enabled": true,
                            "role": "Physician",
                            "authorities": [
                                {
                                    "id": 1,
                                    "roleCode": "USER",
                                    "roleDescription": "User role",
                                    "authority": "USER"
                                },
                                {
                                    "id": 2,
                                    "roleCode": "ADMIN",
                                    "roleDescription": "Admin role",
                                    "authority": "ADMIN"
                                }
                            ],
                            "credentialsNonExpired": true,
                            "accountNonExpired": true,
                            "accountNonLocked": true
                        }
                    },
                    "patient": {
                        "id": 1001,
                        "firstName": "Leanne",
                        "lastName": "Graham",
                        "gender": "Male",
                        "age": 21,
                        "phoneNumber": 9123456780,
                        "email": "leanne.graham@gmail.com",
                        "city": "Bangalore",
                        "state": "Karnataka",
                        "medicalConditions": [
                            {
                                "id": 1,
                                "name": "Cephalalgia (headache)"
                            },
                            {
                                "id": 3,
                                "name": "Otitis externa (swimmer's ear)"
                            },
                            {
                                "id": 9,
                                "name": "Contusion (bruise)"
                            }
                        ]
                    }
                }
            ]
        }
        mock.onGet().reply(200, patient)
        retrievePatientDetailsById(1001).then(response => {
            expect(response.data).toEqual(patient)
        }).catch(error => {
            console.log(error)
        })
    })
    it('check if addNewAllergy is working', () => {
        const allergy = {
            "id": 19,
            "name": "Paracetamol Allergy",
            "reaction": "Tiredness",
            "severity": "MILD",
            "status": "ACTIVE",
            "ingredient": {
                "id": 1,
                "name": "Paracetamol"
            },
            "encounter": {
                "id": 28,
                "date": "2021-11-07",
                "patient": {
                    "id": 1009,
                    "firstName": "Glenna",
                    "lastName": "Reichert",
                    "gender": "Female",
                    "age": 65,
                    "phoneNumber": 9123456788,
                    "email": "glenna.reichert@gmail.com",
                    "city": "Kolkata",
                    "state": "West Bengal",
                    "medicalConditions": [
                        {
                            "id": 6,
                            "name": "Tussis (cough)"
                        },
                        {
                            "id": 7,
                            "name": "Pyrexia (fever)"
                        }
                    ]
                },
                "user": {
                    "id": 1,
                    "username": "John",
                    "password": "$2a$10$uS1MVfXzQkqbY7XDruby/OySJp3yjLmo4QGDSiE30GoN0wGb7DCqS",
                    "email": "john.cage@gmail.com",
                    "enabled": true,
                    "role": "Physician",
                    "authorities": [
                        {
                            "id": 1,
                            "roleCode": "USER",
                            "roleDescription": "User role",
                            "authority": "USER"
                        },
                        {
                            "id": 2,
                            "roleCode": "ADMIN",
                            "roleDescription": "Admin role",
                            "authority": "ADMIN"
                        }
                    ],
                    "credentialsNonExpired": true,
                    "accountNonExpired": true,
                    "accountNonLocked": true
                }
            },
            "patient": {
                "id": 1009,
                "firstName": "Glenna",
                "lastName": "Reichert",
                "gender": "Female",
                "age": 65,
                "phoneNumber": 9123456788,
                "email": "glenna.reichert@gmail.com",
                "city": "Kolkata",
                "state": "West Bengal",
                "medicalConditions": [
                    {
                        "id": 6,
                        "name": "Tussis (cough)"
                    },
                    {
                        "id": 7,
                        "name": "Pyrexia (fever)"
                    }
                ]
            }
        }
        const newAllergy = {
            "encounterId": 0,
            "name": "Paracetamol Allergy",
            "ingredient": { 
                "id": 1,
                "name": "Paracetamol" 
            },
            "reaction": "Tiredness",
            "severity": "MILD",
            "status": "ACTIVE"
        }
        mock.onPost().reply(201, allergy)
        addNewAllergy(1001, newAllergy).then(response => {
            expect(response.data).toEqual(allergy)
        }).catch(error => {
            console.log(error)
        })
    })
    it('check if getRecentPatientsForUser is working', () => {
        const recentPatients = [
            {
                "id": 1009,
                "firstName": "Glenna",
                "lastName": "Reichert",
                "age": 65,
                "gender": "Female",
                "date": "2021-11-07"
            },
            {
                "id": 1008,
                "firstName": "Nicholas",
                "lastName": "Runolfsdottir",
                "age": 21,
                "gender": "Male",
                "date": "2021-11-07"
            },
            {
                "id": 1021,
                "firstName": "Leroy",
                "lastName": "Blair",
                "age": 21,
                "gender": "Male",
                "date": "2021-11-07"
            },
            {
                "id": 1027,
                "firstName": "Alanna",
                "lastName": "Cantrell",
                "age": 65,
                "gender": "Female",
                "date": "2021-11-07"
            },
            {
                "id": 1016,
                "firstName": "Osbert",
                "lastName": "Valdez",
                "age": 32,
                "gender": "Male",
                "date": "2021-11-02"
            },
            {
                "id": 1024,
                "firstName": "Emmett",
                "lastName": "Marshall",
                "age": 45,
                "gender": "Male",
                "date": "2021-11-02"
            }
        ]
        mock.onGet().reply(200, recentPatients)
        getRecentPatientsForUser().then(response => {
            expect(response.data).toEqual(recentPatients)
        }).catch(error => {
            console.log(error)
        })
    })
    it('check if getPrescriptionHistory is working', () => {
        const prescriptionHistory = [
            {
                "brandName": "Tylenol",
                "genericName": "Acetaminophen",
                "dosage": "3 times a day",
                "date": "2021-10-26"
            },
            {
                "brandName": "Advil",
                "genericName": "Ibuprofen",
                "dosage": "3 times a day",
                "date": "2021-10-26"
            },
            {
                "brandName": "Dolo",
                "genericName": "Paracetamol",
                "dosage": "1 time a day",
                "date": "2021-10-26"
            }
        ]
        mock.onGet().reply(200, prescriptionHistory)
        getPrescriptionHistory().then(response => {
            expect(response.data).toEqual(prescriptionHistory)
        }).catch(error => {
            console.log(error)
        })
    })
    it('check if prescribeMedicine is working', () => {
        const prescriptionList = [
            {
                "id": {
                    "encounterId": 29,
                    "medicineId": 1
                },
                "encounter": {
                    "id": 29,
                    "date": "2021-11-07",
                    "patient": {
                        "id": 1009,
                        "firstName": "Glenna",
                        "lastName": "Reichert",
                        "gender": "Female",
                        "age": 65,
                        "phoneNumber": 9123456788,
                        "email": "glenna.reichert@gmail.com",
                        "city": "Kolkata",
                        "state": "West Bengal",
                        "medicalConditions": [
                            {
                                "id": 6,
                                "name": "Tussis (cough)"
                            },
                            {
                                "id": 7,
                                "name": "Pyrexia (fever)"
                            }
                        ]
                    },
                    "user": {
                        "id": 1,
                        "username": "John",
                        "password": "$2a$10$uS1MVfXzQkqbY7XDruby/OySJp3yjLmo4QGDSiE30GoN0wGb7DCqS",
                        "email": "john.cage@gmail.com",
                        "enabled": true,
                        "role": "Physician",
                        "authorities": [
                            {
                                "id": 1,
                                "roleCode": "USER",
                                "roleDescription": "User role",
                                "authority": "USER"
                            },
                            {
                                "id": 2,
                                "roleCode": "ADMIN",
                                "roleDescription": "Admin role",
                                "authority": "ADMIN"
                            }
                        ],
                        "credentialsNonExpired": true,
                        "accountNonExpired": true,
                        "accountNonLocked": true
                    }
                },
                "medicine": {
                    "id": 1,
                    "genericName": "Paracetamol",
                    "brandName": "Dolo",
                    "category": {
                        "id": 1,
                        "name": "Antipyretics"
                    },
                    "ingredients": [
                        {
                            "id": 1,
                            "name": "Paracetamol"
                        }
                    ]
                },
                "dosage": "1 time a day"
            }
        ]
        const prescriptionRequest = {
            "encounterId": 0,
            "name": "Paracetamol Allergy",
            "ingredient": { 
                "id": 1,
                "name": "Paracetamol" 
            },
            "reaction": "Tiredness",
            "severity": "MILD",
            "status": "ACTIVE"
        }
        mock.onPost().reply(201, prescriptionList)
        prescribeMedicine(1001, prescriptionRequest).then(response => {
            expect(response.data).toEqual(prescriptionList)
        }).catch(error => {
            console.log(error)
        })
    })
});