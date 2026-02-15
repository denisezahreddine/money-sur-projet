
export class CustomResponse {
  static readonly loading = 'LOADING';
  static readonly success = 'SUCCESS';
  static readonly error = 'ERROR';

  constructor(
    public type: string,
    public payload?: any // Optionnel car le chargement n'a pas toujours de données
  ) {
  }
}
